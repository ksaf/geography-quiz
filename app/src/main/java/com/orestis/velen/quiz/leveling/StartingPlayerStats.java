package com.orestis.velen.quiz.leveling;

public class StartingPlayerStats {

    public static String getStartingPlayer() {
        return "{\n" +
                "  \"currentLevel\": 1,\n" +
                "  \"currentXP\": 0,\n" +
                "  \"gameTheme\": \"GEOGRAPHY\",\n" +
                "  \"powers\": {\n" +
                "    \"SKIP\": {\n" +
                "      \"powerLevel\": 0,\n" +
                "      \"powerType\": \"SKIP\"\n" +
                "    }, \n" +
                "    \"FIFTY_FIFTY\": {\n" +
                "      \"powerLevel\": 0,\n" +
                "      \"powerType\": \"FIFTY_FIFTY\"\n" +
                "    }, \n" +
                "    \"SHIELD\": {\n" +
                "      \"powerLevel\": 0,\n" +
                "      \"powerType\": \"SHIELD\"\n" +
                "    },\n" +
                "    \"FREEZE_TIME\": {\n" +
                "      \"powerLevel\": 0,\n" +
                "      \"powerType\": \"FREEZE_TIME\"\n" +
                "    },\n" +
                "    \"EXTRA_TIME\": {\n" +
                "      \"powerLevel\": 0,\n" +
                "      \"powerType\": \"EXTRA_TIME\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
