package net.hexaway.board.abstraction;

import net.hexaway.board.abstraction.util.QueueSnapshot;
import net.hexaway.board.abstraction.util.TickCounter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ScoreboardElement {

    Map<String, Integer> getData();

    QueueSnapshot<ElementFrame> getQueueSnapshot();

    ElementFrame current();

    String getCurrentFrameContent();

    TickCounter getCurrentWait();

    boolean update();

    ScoreboardElement copy();

    static ScoreboardElement of(Map<String, Integer> content) {
        return new SimpleScoreboardElement(content);
    }

    static ScoreboardElement withSameInterval(int updateInterval, List<String> frames) {
        Map<String, Integer> contentMap = new LinkedHashMap<>();

        for (String frame : frames) {
            contentMap.put(frame, updateInterval);
        }

        return of(contentMap);
    }

    static ScoreboardElement singleFrame(String content) {
        return of(Collections.singletonMap(content, 1));
    }

    static ScoreboardElement simple(String str) {
        return singleFrame(str);
    }

    class ElementFrame {

        private final String content;
        private final int duration;

        public ElementFrame(String content, int duration) {
            this.content = content;
            this.duration = duration;
        }

        public String getContent() {
            return content;
        }

        public int getDuration() {
            return duration;
        }
    }
}