package com.orestis.velen.quiz.player;

import android.content.Context;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.orestis.velen.quiz.helpPowers.Power;
import com.orestis.velen.quiz.leaderboard.GoogleLeaderboard;
import com.orestis.velen.quiz.leveling.LevelExperienceMetrics;
import com.orestis.velen.quiz.leveling.LevelExperienceMetricsFactory;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.repositories.GameTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Player{

    @Expose
    private int currentXP;
    @Expose
    private int currentLevel;

    private GameTheme gameTheme;
    @Expose
    private Map<String, Power> powers;

    @Expose
    private int remainingSkillPoints;

    @Expose
    private int highScore;

    public Player() {}

    private Player(GameTheme gameTheme) {
        this.gameTheme = gameTheme;
    }

    public List<Integer> addXP(int xp) {
        List<Integer> xpRangesPassed = new ArrayList<>();
        xpRangesPassed.add(getXpToLevel());
        int xpForNextLevel = getXpToLevel() - currentXP;
        while (xp >= xpForNextLevel) {
            xp -= xpForNextLevel;
            currentLevel++;
            remainingSkillPoints++;
            currentXP = 0;
            xpForNextLevel = getXpToLevel();
            xpRangesPassed.add(getXpToLevel());
        }
        currentXP += xp;
        UserSession.getInstance().savePlayer(this);
        return xpRangesPassed;
    }

    @Exclude
    public int getXpToLevel() {
        LevelExperienceMetricsFactory metricsFactory = new LevelExperienceMetricsFactory();
        LevelExperienceMetrics metrics = metricsFactory.getLevelExperienceMetrics(GameTheme.GEOGRAPHY);
        return metrics.getExperienceForLevelUp(currentLevel);
    }

    public int getRemainingSkillPoints() {
        return remainingSkillPoints;
    }

    public void setRemainingSkillPoints(int remainingSkillPoints) {
        this.remainingSkillPoints = remainingSkillPoints;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Map<String, Power> getPowers() {
        return powers;
    }

    public void save(){
        UserSession.getInstance().savePlayer(this);
    }

    public void saveHighScore(Context context, int score) {
        GoogleLeaderboard leaderboard = new GoogleLeaderboard();
        leaderboard.updateLeaderboard(context, score);
        if(score > highScore) {
            highScore = score;
        }
        save();
    }

    public int getHighScore() {
        return highScore;
    }

    public boolean hasXpBoostEnabled(int xpBoostDuration) {
        long timeNow = new Date().getTime();
        long xpBoostEnabledTime = UserSession.getInstance().getXpBoostEnabledTime();
        return timeNow < xpBoostEnabledTime + xpBoostDuration;
    }

    public long getXpBoostEnabledTimeLeft(int xpBoostDuration) {
        long timePassed = new Date().getTime() - UserSession.getInstance().getXpBoostEnabledTime();
        long timeLeft = xpBoostDuration - timePassed;
        return timeLeft > 0 ? timeLeft : 0;
    }

    public void saveXpBoostEnabledTime() {
        UserSession.getInstance().saveXpBoostEnabledTime();
    }

    public static class Builder {
        private int currentXP;
        private int currentLevel;
        private int remainingSkillPoints;
        private GameTheme gameTheme;

        public Builder withXP(int currentXP) {
            this.currentXP = currentXP;
            return this;
        }

        public Builder withRemainingSkillPoints(int remainingSkillPoints) {
            this.remainingSkillPoints = remainingSkillPoints;
            return this;
        }

        public Builder withLevel(int currentLevel) {
            this.currentLevel = currentLevel;
            return this;
        }

        public Builder forTheme(GameTheme gameTheme) {
            this.gameTheme = gameTheme;
            return this;
        }

        public Player build() {
            Player player = new Player(gameTheme);
            player.currentXP = this.currentXP;
            player.currentLevel = this.currentLevel;
            player.remainingSkillPoints = this.remainingSkillPoints;

            return player;
        }
    }

}
