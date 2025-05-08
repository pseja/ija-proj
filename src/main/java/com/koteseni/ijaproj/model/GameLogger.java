package com.koteseni.ijaproj.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameLogger {
    private static final String SAVES_DIRECTORY = "data/saves";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final Board initial_board;
    private final int difficulty;
    private final List<Move> moves;
    private final LocalDateTime start_time;

    public GameLogger(Board board, int difficulty) {
        this.initial_board = board.deepCopy();
        this.difficulty = difficulty;
        this.moves = new ArrayList<>();
        this.start_time = LocalDateTime.now();
    }

    public void logMove(int row, int col) {
        moves.add(new Move(row, col, System.currentTimeMillis()));
    }

    public String saveGame() throws IOException {
        checkSavesDirectory();

        String filename = start_time.format(DATE_FORMAT) + ".json";
        Path save_file_path = Paths.get(SAVES_DIRECTORY, filename);

        JsonObject game_data = new JsonObject();

        game_data.addProperty("rows", initial_board.getRows());
        game_data.addProperty("cols", initial_board.getCols());
        game_data.addProperty("difficulty", difficulty);
        game_data.addProperty("start_time", start_time.format(DATE_FORMAT));

        JsonArray initial_board_state = saveBoardState(initial_board);
        game_data.add("initial_board", initial_board_state);

        JsonArray moves_array = new JsonArray();
        for (Move move : moves) {
            JsonObject move_json_object = new JsonObject();

            move_json_object.addProperty("row", move.getRow());
            move_json_object.addProperty("col", move.getCol());
            move_json_object.addProperty("timestamp", move.getTimestamp());

            moves_array.add(move_json_object);
        }
        game_data.add("moves", moves_array);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(save_file_path.toFile())) {
            writer.write(gson.toJson(game_data));
        }

        return save_file_path.toString();
    }

    public static GameState loadGame(String file_path) throws IOException {
        JsonObject game_data;

        try (FileReader reader = new FileReader(file_path)) {
            JsonParser parser = new JsonParser();
            game_data = parser.parse(reader).getAsJsonObject();
        }

        int rows = game_data.get("rows").getAsInt();
        int cols = game_data.get("cols").getAsInt();
        int difficulty = game_data.get("difficulty").getAsInt();
        String start_time_str = game_data.get("start_time").getAsString();

        LocalDateTime start_time = LocalDateTime.parse(start_time_str, DATE_FORMAT);

        JsonArray initial_board_json_array = game_data.get("initial_board").getAsJsonArray();

        List<Move> moves = new ArrayList<>();
        JsonArray moves_array = game_data.get("moves").getAsJsonArray();
        for (int i = 0; i < moves_array.size(); i++) {
            JsonObject move_json_object = moves_array.get(i).getAsJsonObject();

            int row = move_json_object.get("row").getAsInt();
            int col = move_json_object.get("col").getAsInt();
            long timestamp = move_json_object.get("timestamp").getAsLong();

            moves.add(new Move(row, col, timestamp));
        }

        return new GameState(rows, cols, difficulty, start_time, initial_board_json_array, moves);
    }

    public static List<Path> getSavedGames() throws IOException {
        checkSavesDirectory();

        Path saves_directory = Paths.get(SAVES_DIRECTORY);
        List<Path> saved_games = new ArrayList<>();

        Files.list(saves_directory)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(saved_games::add);

        return saved_games;
    }

    private JsonArray saveBoardState(Board board) {
        JsonArray board_json_array = new JsonArray();
        Tile[][] tiles = board.getTiles();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Tile tile = tiles[row][col];
                if (tile == null) {
                    continue;
                }

                board_json_array.add(saveTile(tile));
            }
        }

        return board_json_array;
    }

    private JsonObject saveTile(Tile tile) {
        if (tile == null) {
            return null;
        }

        JsonObject tile_json_object = new JsonObject();

        tile_json_object.addProperty("row", tile.getRow());
        tile_json_object.addProperty("col", tile.getCol());
        JsonArray connections_json_array = new JsonArray();
        for (Direction dir : tile.getConnections()) {
            connections_json_array.add(dir.name());
        }
        tile_json_object.add("connections", connections_json_array);
        tile_json_object.addProperty("powered", tile.isPowered());
        tile_json_object.addProperty("rotation_count", tile.getRotationCount());

        switch (tile) {
            case Source source -> {
                tile_json_object.addProperty("type", "source");
                tile_json_object.addProperty("shape", source.getShape().name());
            }
            case LightBulb light_bulb -> {
                tile_json_object.addProperty("type", "light_bulb");
                tile_json_object.addProperty("direction", light_bulb.getDirection().name());
            }
            case Wire wire -> {
                tile_json_object.addProperty("type", "wire");
                tile_json_object.addProperty("shape", wire.getShape().name());
            }
            default -> {
                throw new IllegalArgumentException("Unknown tile type: " + tile.getClass().getName());
            }
        }

        return tile_json_object;
    }

    private static void checkSavesDirectory() throws IOException {
        Path saves_directory = Paths.get(SAVES_DIRECTORY);
        if (!Files.exists(saves_directory)) {
            Files.createDirectories(saves_directory);
        }
    }
}