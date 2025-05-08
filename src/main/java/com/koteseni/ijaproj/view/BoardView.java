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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Handles rendering the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class BoardView {

    /** Standard width for the grid pane. */
    private static final double GRID_WIDTH = 500;

    /** Standard height for the grid pane. */
    private static final double GRID_HEIGHT = 500;

    /** The grid pane where tiles are rendered. */
    private final GridPane grid_pane;

    /** The game board. */
    private final Board board;

    /** The controller handling game logic. */
    private final GameController game_controller;

    /** Size of individual tiles, calculated based on board dimensions. */
    private final double tile_size;

    /** Cache for loaded images to improve performance. */
    private final Map<String, Image> image_cache = new HashMap<>();

    /**
     * Creates a new BoardView for rendering a game board.
     *
     * @param grid_pane       The grid pane where tiles will be rendered
     * @param board           The board model to render
     * @param game_controller The controller handling game logic
     */
    public BoardView(GridPane grid_pane, Board board, GameController game_controller) {
        this.grid_pane = grid_pane;
        this.board = board;
        this.game_controller = game_controller;
        tile_size = Math.floor(Math.min(GRID_WIDTH / board.getCols(), GRID_HEIGHT / board.getRows()));
    }

    /**
     * Updates the view to show the current state of the board.
     * 
     * <p>
     * This method is called every time the board changes (tile rotation, power
     * propagation, hint overlay).
     * </p>
     */
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

    /**
     * Creates a StackPane representing a single tile on the board.
     * 
     * <p>
     * Sets up click handling for player interaction and contains:
     * <ol>
     * <li>A background image (grass or redstone block)</li>
     * <li>An image representing the tile itself</li>
     * <li>Optional hint overlay if hint mode is enabled</li>
     * </ol>
     * </p>
     *
     * @param tile The tile to render, or null for an empty space
     * @param row  The row position of the tile
     * @param col  The column position of the tile
     * 
     * @return A StackPane representing the tile
     */
    private StackPane createTilePane(Tile tile, int row, int col) {
        StackPane stack_pane = new StackPane();
        stack_pane.setPrefSize(tile_size, tile_size);
        stack_pane.setMaxSize(tile_size, tile_size);
        stack_pane.setMinSize(tile_size, tile_size);

        Image background_image;
        if (tile instanceof Source) {
            background_image = getImage("redstone_block.png");
        } else {
            background_image = getImage("grass_block_top.png");
        }

        if (background_image != null) {
            ImageView background_view = new ImageView(background_image);
            background_view.setFitWidth(tile_size);
            background_view.setFitHeight(tile_size);
            stack_pane.getChildren().add(background_view);
        }

        if (tile != null) {
            String tile_image_name = getTileImageName(tile);
            Image tile_image = getImage(tile_image_name);
            if (tile_image != null) {
                ImageView tile_view = new ImageView(tile_image);
                tile_view.setFitWidth(tile_size);
                tile_view.setFitHeight(tile_size);
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

    /**
     * Adds a hint overlay to a tile showing rotation information.
     *
     * @param stack_pane The StackPane representing the tile
     * @param tile       The tile to show hints for
     */
    private void showHintOverlay(StackPane stack_pane, Tile tile) {
        int rotations_needed = tile.getRotationsToCorrect();
        int total_rotations = tile.getPlayerRotationCount();

        Label hint_label = new Label(total_rotations + "    " + rotations_needed);

        if (rotations_needed == 0) {
            hint_label.getStyleClass().add("hint-correct");
        } else {
            hint_label.getStyleClass().remove("hint-correct");
        }

        double font_size = Math.max(13, tile_size / 5);
        hint_label.setFont(Font.font("Minecraft", FontWeight.BOLD, font_size));

        StackPane hint_pane = new StackPane(hint_label);
        hint_pane.setAlignment(Pos.CENTER);
        stack_pane.getChildren().add(hint_pane);
        StackPane.setAlignment(hint_pane, Pos.TOP_CENTER);
    }

    /**
     * Calculates the proper rotation angle for a tile image.
     * 
     * @param tile The tile to calculate rotation for
     * @return The rotation angle in degrees (0, 90, 180, or 270)
     */
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

    /**
     * Loads and caches an image from the assets/ directory.
     *
     * @param filename The name of the image file
     * @return The loaded image, or null if the image could not be loaded
     */
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

    /**
     * Determines the image filename for a tile.
     *
     * @param tile The tile to get an image for
     * @return The filename of the appropriate image
     */
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
