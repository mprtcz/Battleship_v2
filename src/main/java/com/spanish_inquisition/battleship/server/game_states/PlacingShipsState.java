package com.spanish_inquisition.battleship.server.game_states;

import com.spanish_inquisition.battleship.common.Header;
import com.spanish_inquisition.battleship.common.NetworkMessage;
import com.spanish_inquisition.battleship.server.Player;
import com.spanish_inquisition.battleship.server.Players;
import com.spanish_inquisition.battleship.server.bus.MessageBus;
import com.spanish_inquisition.battleship.server.database.DatabaseController;
import com.spanish_inquisition.battleship.server.database.PlayerEntity;
import com.spanish_inquisition.battleship.server.database.PlayerEntityController;
import com.spanish_inquisition.battleship.server.fleet.FleetBuilder;
import com.spanish_inquisition.battleship.server.fleet.FleetValidator;

import java.util.LinkedList;
import java.util.List;

import static com.spanish_inquisition.battleship.common.AppLogger.DEFAULT_LEVEL;
import static com.spanish_inquisition.battleship.common.AppLogger.logger;
import static com.spanish_inquisition.battleship.server.BattleshipServer.SERVER_ID;

public class PlacingShipsState extends GameState {
    public PlacingShipsState(Players players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        List<Player> notReadyPlayers = new LinkedList<>(players.getBothPlayers());
        while (!notReadyPlayers.isEmpty()) {
            for (Player player : notReadyPlayers) {
                if (requestBus.haveMessageFromSender(player.getPlayerId())) {
                    resolvePlayerFleetValidation(player, notReadyPlayers);
                }
            }
        }
        sendPlayersScores();
        return new PlayerActionState(players, requestBus);
    }

    private void resolvePlayerFleetValidation(Player player, List<Player> notReadyPlayers) {
        String fleetMessage = requestBus.getMessageFrom(player.getPlayerId()).getContent();
        boolean isValidFleet = validateFleet(fleetMessage);
        if (isValidFleet) {
            player.setFleet(new FleetBuilder().build(fleetMessage));
            notReadyPlayers.remove(player);
            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_VALID.name());
        } else {
            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_INVALID.name());
        }
    }

    private boolean validateFleet(String fleetMessage) {
        return fleetMessage.contains(Header.FLEET_REQUEST.name()) && FleetValidator.validate(fleetMessage);
    }

    public void sendPlayersScores() {
        PlayerEntityController playerEntityController = new PlayerEntityController(new DatabaseController());
        List<PlayerEntity> playerRecords = playerEntityController.getPlayersFromDB();
        logger.log(DEFAULT_LEVEL, "Read records from db:" + playerRecords);
        for (PlayerEntity playerEntity: playerRecords) {
            NetworkMessage networkMessage = new NetworkMessage(Header.SCORE, playerEntity.getReadyToSendRepresentation());
            requestBus.addMessage(SERVER_ID, players.getCurrentPlayer().getPlayerId(), NetworkMessage.Parser.parseMessageToString(networkMessage));
            requestBus.addMessage(SERVER_ID, players.getOpponentOf(players.getCurrentPlayer()).getPlayerId(),
                    NetworkMessage.Parser.parseMessageToString(networkMessage));
        }
    }
}