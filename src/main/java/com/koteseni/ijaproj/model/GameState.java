// Class representing the state of a game and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Represents the state of a game.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class GameState {
    /** Number of rows in the game board. */
    private final int rows;

    /** Number of columns in the game board. */
    private final int cols;

    /** Difficulty level of the game. */
    private final int difficulty;

    /** Timestamp when the game was started. */
    private final LocalDateTime start_time;

    /** JSON array representation of the initial board state. */
    private final JsonArray initial_board_state;

    /** List of moves made by the player in chronological order. */
    private final List<Move> moves;

    /**
     * Creates a new GameState with the specified parameters.
     *
     * @param rows                Number of rows in the game board
     * @param cols                Number of columns in the game board
     * @param difficulty          Difficulty level of the game
     * @param start_time          Timestamp when the game was started
     * @param initial_board_state JSON array representation of the initial board
     *                            state
     * @param moves               List of moves made by the player
     */
    public GameState(int rows, int cols, int difficulty, LocalDateTime start_time, JsonArray initial_board_state,
            List<Move> moves) {

        this.rows = rows;
        this.cols = cols;
        this.difficulty = difficulty;
        this.start_time = start_time;
        this.initial_board_state = initial_board_state;
        this.moves = moves;
    }

    /**
     * Creates a Board object from the initial board state stored in this GameState.
     * 
     * <p>
     * Deserializes the JSON representation of the initial board state into a
     * functional Board object.
     * </p>
     *
     * @return A new Board object representing the initial state of the game
     */
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

    /**
     * Applies moves to a board up to the specified move index.
     * 
     * <p>
     * Used for replaying a game.
     * </p>
     *
     * @param board      The board on which to apply the moves
     * @param move_index The index of the last move (inclusive) to apply
     */
    public void applyMoves(Board board, int move_index) {
        if (move_index < 0 || move_index >= moves.size()) {
            return;
        }

        for (int i = 0; i <= move_index; i++) {
            Move move = moves.get(i);
            board.turnTile(move.getRow(), move.getCol());
        }
    }

    /**
     * Gets the total number of moves in this game.
     *
     * @return The number of moves
     */
    public int getTotalMoves() {
        return moves.size();
    }

    /**
     * Gets the difficulty level of this game.
     *
     * @return The difficulty level
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the timestamp when this game was started.
     *
     * @return The start time
     */
    public LocalDateTime getStartTime() {
        return start_time;
    }

    /**
     * Gets the number of rows in the game board.
     *
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the game board.
     *
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the JSON array representation of the initial board state.
     *
     * @return The initial board state as a JSON array
     */
    public JsonArray getInitialBoardState() {
        return initial_board_state;
    }

    /**
     * Gets the list of moves made by the player.
     *
     * @return The list of moves
     */
    public List<Move> getMoves() {
        return moves;
    }
}
