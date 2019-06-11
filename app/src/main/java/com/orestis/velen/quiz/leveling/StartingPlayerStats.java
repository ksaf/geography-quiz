package com.orestis.velen.quiz.leveling;

public class StartingPlayerStats {

    public static String getStartingPlayer() {
        return "{\n" +
                "  \"currentLevel\": 1,\n" +
                "  \"currentXP\": 0,\n" +
                "  \"gameTheme\": \"GEOGRAPHY\",\n" +
                "  \"powers\": {\n" +
                "    \"SKIP\": {\n" +
                "      \"available\": true,\n" +
                "      \"charges\": 1,\n" +
                "      \"powerLevel\": 1,\n" +
                "      \"powerType\": \"SKIP\"\n" +
                "    }, \n" +
                "    \"FIFTY_FIFTY\": {\n" +
                "      \"available\": false,\n" +
                "      \"charges\": 1,\n" +
                "      \"powerLevel\": 1,\n" +
                "      \"powerType\": \"FIFTY_FIFTY\"\n" +
                "    }, \n" +
                "    \"SHIELD\": {\n" +
                "      \"available\": false,\n" +
                "      \"charges\": 1,\n" +
                "      \"powerLevel\": 1,\n" +
                "      \"powerType\": \"SHIELD\"\n" +
                "    },\n" +
                "    \"FREEZE_TIME\": {\n" +
                "      \"available\": false,\n" +
                "      \"charges\": 1,\n" +
                "      \"powerLevel\": 1,\n" +
                "      \"powerType\": \"FREEZE_TIME\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
