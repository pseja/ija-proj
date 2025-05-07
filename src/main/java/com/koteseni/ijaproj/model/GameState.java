package com.koteseni.ijaproj.model;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GameState {
    private final int rows;
    private final int cols;
    private final int difficulty;
    private final LocalDateTime start_time;
    private final JsonArray initial_board_state;
    private final List<Move> moves;

    public GameState(int rows, int cols, int difficulty, LocalDateTime start_time, JsonArray initial_board_state,
            List<Move> moves) {

        this.rows = rows;
        this.cols = cols;
        this.difficulty = difficulty;
        this.start_time = start_time;
        this.initial_board_state = initial_board_state;
        this.moves = moves;
    }

    public Board createInitialBoard() {
        Board board = new Board(rows, cols);

        for (int i = 0; i < initial_board_state.size(); i++) {
            JsonObject tile_json_object = initial_board_state.get(i).getAsJsonObject();

            int row = tile_json_object.get("row").getAsInt();
            int col = tile_json_object.get("col").getAsInt();
            EnumSet<Direction> connections = EnumSet.noneOf(Direction.class);
            JsonArray connections_json_array = tile_json_object.get("connections").getAsJsonArray();
            for (int j = 0; j < connections_json_array.size(); j++) {
                connections.add(Direction.valueOf(connections_json_array.get(j).getAsString()));
            }
            boolean powered = tile_json_object.get("powered").getAsBoolean();
            int rotationCount = tile_json_object.get("rotation_count").getAsInt();
            String type = tile_json_object.get("type").getAsString();

            Tile tile = null;

            switch (type) {
                case "source" -> {
                    WireShape shape = WireShape.valueOf(tile_json_object.get("shape").getAsString());
                    tile = new Source(row, col, shape);
                    if (!connections.isEmpty()) {
                        tile.setConnections(connections);
                    }
                }
                case "light_bulb" -> {
                    Direction direction = Direction.valueOf(tile_json_object.get("direction").getAsString());
                    tile = new LightBulb(row, col, direction);
                    if (!connections.isEmpty()) {
                        tile.setConnections(connections);
                    }
                }
                case "wire" -> {
                    WireShape shape = WireShape.valueOf(tile_json_object.get("shape").getAsString());
                    tile = new Wire(row, col, shape);
                    if (!connections.isEmpty()) {
                        tile.setConnections(connections);
                    }
                }
            }

            if (tile == null) {
                continue;
            }

            tile.setPowered(powered);
            tile.setRotationCount(rotationCount);

            board.setTile(row, col, tile);
        }

        board.propagatePower();

        return board;
    }

    public void applyMoves(Board board, int move_index) {
        if (move_index < 0 || move_index >= moves.size()) {
            return;
        }

        for (int i = 0; i <= move_index; i++) {
            Move move = moves.get(i);
            board.turnTile(move.getRow(), move.getCol());
        }
    }

    public int getTotalMoves() {
        return moves.size();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public LocalDateTime getStartTime() {
        return start_time;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public JsonArray getInitialBoardState() {
        return initial_board_state;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
