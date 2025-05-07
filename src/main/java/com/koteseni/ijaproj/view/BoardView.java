package com.koteseni.ijaproj.view;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.koteseni.ijaproj.controller.GameController;
import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.Direction;
import com.koteseni.ijaproj.model.LightBulb;
import com.koteseni.ijaproj.model.Source;
import com.koteseni.ijaproj.model.Tile;
import com.koteseni.ijaproj.model.Wire;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardView {

    // default 64 for now, make it so that it scales with the window size somehow
    private static final double TILE_SIZE = 64;

    private final GridPane grid_pane;
    private final Board board;
    private final GameController game_controller;

    private final Map<String, Image> image_cache = new HashMap<>();

    public BoardView(GridPane grid_pane, Board board, GameController game_controller) {
        this.grid_pane = grid_pane;
        this.board = board;
        this.game_controller = game_controller;
    }

    public void updateView() {
        grid_pane.getChildren().clear();

        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board.getTile(row, col);
                StackPane tile_pane = createTilePane(tile, row, col);
                grid_pane.add(tile_pane, col, row);
            }
        }
    }

    private StackPane createTilePane(Tile tile, int row, int col) {
        StackPane stack_pane = new StackPane();
        stack_pane.setPrefSize(TILE_SIZE, TILE_SIZE);
        stack_pane.setMaxSize(TILE_SIZE, TILE_SIZE);
        stack_pane.setMinSize(TILE_SIZE, TILE_SIZE);

        Image background_image;
        if (tile instanceof Source) {
            background_image = getImage("redstone_block.png");
        } else {
            background_image = getImage("grass_block_top.png");
        }

        if (background_image != null) {
            ImageView background_view = new ImageView(background_image);
            background_view.setFitWidth(TILE_SIZE);
            background_view.setFitHeight(TILE_SIZE);
            stack_pane.getChildren().add(background_view);
        }

        if (tile != null) {
            String tile_image_name = getTileImageName(tile);
            Image tile_image = getImage(tile_image_name);
            if (tile_image != null) {
                ImageView tile_view = new ImageView(tile_image);
                tile_view.setFitWidth(TILE_SIZE);
                tile_view.setFitHeight(TILE_SIZE);
                tile_view.setRotate(calculateRotation(tile));
                stack_pane.getChildren().add(tile_view);
            }

            if (game_controller != null && game_controller.areHintsEnabled()) {
                showHintOverlay(stack_pane, tile);
            }

            // handling clicks only when the game is not in replay mode
            if (game_controller != null) {
                stack_pane.setOnMouseClicked(event -> {
                    game_controller.handleTileClick(row, col);
                });
            }
        }

        return stack_pane;
    }

    private void showHintOverlay(StackPane stack_pane, Tile tile) {
        int rotations_needed = tile.getRotationsToCorrect();
        int total_rotations = tile.getPlayerRotationCount();

        Label hint_label = new Label(total_rotations + "    " + rotations_needed);
        hint_label.setTextFill(rotations_needed == 0 ? Color.LIGHTGREEN : Color.WHITE);
        hint_label.setFont(Font.font("System", FontWeight.BOLD, 13));

        StackPane hint_pane = new StackPane(hint_label);
        hint_pane.setAlignment(Pos.CENTER);
        stack_pane.getChildren().add(hint_pane);
        StackPane.setAlignment(hint_pane, Pos.TOP_CENTER);
    }

    private int calculateRotation(Tile tile) {
        EnumSet<Direction> connections = tile.getConnections();

        return switch (tile) {
            case Wire wire -> switch (wire.getShape()) {
                case I -> connections.contains(Direction.NORTH) ? 0 : 90;
                case L -> {
                    if (connections.containsAll(EnumSet.of(Direction.SOUTH, Direction.WEST))) {
                        yield 0;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.WEST, Direction.NORTH))) {
                        yield 90;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.NORTH, Direction.EAST))) {
                        yield 180;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.EAST, Direction.SOUTH))) {
                        yield 270;
                    }

                    yield 0;
                }
                case T -> {
                    if (connections.containsAll(EnumSet.of(Direction.SOUTH, Direction.WEST, Direction.NORTH))) {
                        yield 0;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.WEST, Direction.NORTH, Direction.EAST))) {
                        yield 90;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH))) {
                        yield 180;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.EAST, Direction.SOUTH, Direction.WEST))) {
                        yield 270;
                    }

                    yield 0;
                }
                case X -> {
                    yield tile.getRotationCount() * 90;
                }
            };
            case Source source -> switch (source.getShape()) {
                case I -> connections.contains(Direction.NORTH) ? 0 : 90;
                case L -> {
                    if (connections.containsAll(EnumSet.of(Direction.SOUTH, Direction.WEST))) {
                        yield 0;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.WEST, Direction.NORTH))) {
                        yield 90;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.NORTH, Direction.EAST))) {
                        yield 180;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.EAST, Direction.SOUTH))) {
                        yield 270;
                    }

                    yield 0;
                }
                case T -> {
                    if (connections.containsAll(EnumSet.of(Direction.SOUTH, Direction.WEST, Direction.NORTH))) {
                        yield 0;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.WEST, Direction.NORTH, Direction.EAST))) {
                        yield 90;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH))) {
                        yield 180;
                    }
                    if (connections.containsAll(EnumSet.of(Direction.EAST, Direction.SOUTH, Direction.WEST))) {
                        yield 270;
                    }

                    yield 0;
                }
                case X -> tile.getRotationCount() * 90;
            };
            case LightBulb bulb -> switch (bulb.getDirection()) {
                case NORTH -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
            };

            default -> 0;
        };
    }

    private Image getImage(String filename) {
        if (image_cache.containsKey(filename)) {
            return image_cache.get(filename);
        }

        String path = "/com/koteseni/ijaproj/assets/" + filename;
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Error loading image: " + path);
            return null;
        }

        Image image = new Image(stream);
        image_cache.put(filename, image);

        return image;
    }

    private String getTileImageName(Tile tile) {
        String component_name = "";
        String suffix = tile.isPowered() ? "_on.png" : "_off.png";

        if (tile instanceof Wire wire) {
            component_name = "redstone_" + wire.getShape().name();
        } else if (tile instanceof Source source) {
            component_name = "redstone_" + source.getShape().name();
        } else if (tile instanceof LightBulb) {
            component_name = "redstone_torch";
        }

        return component_name + suffix;
    }
}
