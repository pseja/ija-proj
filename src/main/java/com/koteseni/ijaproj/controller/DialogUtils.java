package com.koteseni.ijaproj.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class for displaying dialog boxes.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class DialogUtils {

    /**
     * Shows an information dialog with the specified message.
     *
     * @param message The message to display
     */
    public static void showInfoBox(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information dialog with the specified header and message.
     *
     * @param header_message The header message
     * @param message        The content message
     */
    public static void showInfoBox(String header_message, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header_message);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog with the specified message.
     *
     * @param message The error message to display
     */
    public static void showErrorBox(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
