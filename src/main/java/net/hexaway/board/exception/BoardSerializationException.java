package net.hexaway.board.exception;

public class BoardSerializationException extends RuntimeException {

    private final String boardField;

    public BoardSerializationException(String boardField) {
        this.boardField = boardField;
    }

    public String getBoardField() {
        return boardField;
    }
}