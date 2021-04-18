package net.hexaway.board;

public class ScoreboardTeam {

    private String text;

    private String prefix;

    private String suffix;

    public ScoreboardTeam(String text) {
        applyProperties(text);
    }

    public String get() {
        return text;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    private void applyProperties(String text) {
        if (text.length() < 16) {
            this.prefix = "";
            this.text = text;
            this.suffix = "";
        } else if (text.length() < 32) {
            this.prefix = text.substring(0, 15);
            this.text = text.substring(15);
            this.suffix = "";
        } else if (text.length() <= 48) {
            this.prefix = text.substring(0, 15);
            this.text = text.substring(15, 31);
            this.suffix = text.substring(31);
        } else {
            applyProperties(text.substring(47));
        }
    }

    public String getFullText() {
        return prefix + text + suffix;
    }
}