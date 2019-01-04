package io.github.patpatchpatrick.alphapigeon.levels;

import com.badlogic.gdx.math.MathUtils;

import io.github.patpatchpatrick.alphapigeon.dodgeables.Dodgeables;
import io.github.patpatchpatrick.alphapigeon.dodgeables.Notifications;
import io.github.patpatchpatrick.alphapigeon.dodgeables.Notifications.ExclamationMark;

public class LevelThree extends Level {

    // The third level of the game is considered the "MEDIUM" level.
    // This is the level where things begin to get very difficult
    // This level consists of random waves/puzzles of dodgeable creatures
    // that are randomly selected

    //RANDOM WAVE VARIABLES
    // Level Three consists of "MEDIUM" difficulty waves of dodgeables that occur randomly
    private final int TOTAL_NUMBER_OF_WAVES = 6;
    private final float RANDOM_WAVE_UFO_HORIZONTAL = 1f;
    private boolean randomWaveHorizUfoSpawned = false;
    private final float RANDOM_WAVE_UFO_HORIZ_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_UFO_VERTICAL = 2f;
    private boolean randomWaveVertUfoSpawned = false;
    private final float RANDOM_WAVE_UFO_VERT_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_METEORS = 3f;
    private final float RANDOM_WAVE_METEORS_SPAWN_DURATION = 2000f;
    private final float RANDOM_WAVE_METEORS_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_MISSILES = 4f;
    private final float RANDOM_WAVE_MISSILES_SPAWN_DURATION = 2000f;
    private final float RANDOM_WAVE_MISSILES_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_UFO_CENTER = 5f;
    private final float RANDOM_WAVE_UFO_CENTER_SPAWN_DURATION = 30000f;
    private boolean randomWaveCenterUfoSpawned = false;
    private final float RANDOM_WAVE_UFO_CENTER_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_VERT_UFO_TELEPORT = 6f;
    private boolean randomWaveVertTeleUfoSpawned = false;
    private boolean randomWaveVertTeleUfoTeleportsSpawned = false;
    private long randomWaveVerTeleUfoTimeUfoSpawned = 999999999;
    private final float RANDOM_WAVE_VERT_UFO_TELEPORT_TIME_BEFORE_TELE_SPAWN = 12000f;
    private final float RANDOM_WAVE_VERT_UFO_TELEPORT_TOTAL_TIME = 30000f;
    private final float RANDOM_WAVE_L1BIRD_SPAWN_DURATION = 2000;
    private final float RANDOM_WAVE_L2BIRD_SPAWN_DURATION = 2000;


    public LevelThree(Dodgeables dodgeables) {
        super(dodgeables);
    }

    public void run(float totalGameTime, long currentTimeInMillis, float powerUpShieldInterval) {

        this.totalGameTime = totalGameTime;
        this.currentTimeInMillis = currentTimeInMillis;
        this.powerUpShieldInterval = powerUpShieldInterval;

        if (!randomWaveIsInitiated) {
            resetWaveVariables();
            //If a random isn't currently in progress:
            //Generate a random number to determine which random wave to run
            randomWave = MathUtils.random(1, TOTAL_NUMBER_OF_WAVES);
            //Save the time the last random wave was started
            lastRandomWaveStartTime = currentTimeInMillis;
            randomWaveIsInitiated = true;
        } else if (randomWave == RANDOM_WAVE_UFO_HORIZONTAL) {
            runRandomWaveHorizontalUFO();
        } else if (randomWave == RANDOM_WAVE_UFO_VERTICAL) {
            runRandomWaveVerticalUFO();
        } else if (randomWave == RANDOM_WAVE_METEORS) {
            runRandomWaveMeteors();
        } else if (randomWave == RANDOM_WAVE_MISSILES) {
            runRandomWaveMissiles();
        } else if (randomWave == RANDOM_WAVE_UFO_CENTER) {
            runRandomWaveCenterUFO();
        } else if (randomWave == RANDOM_WAVE_VERT_UFO_TELEPORT) {
            runRandomWaveVerticalUFOAndTeleport();
        }

    }

    private void resetWaveVariables() {
        //Reset all variables that need to be reset before selecting a new wave
        randomWaveHorizUfoSpawned = false;
        randomWaveVertUfoSpawned = false;
        randomWaveCenterUfoSpawned = false;
        randomWaveVertTeleUfoSpawned = false;
        randomWaveVertTeleUfoTeleportsSpawned = false;
        ExclamationMark.notificationSpawned = false;
        randomWaveVerTeleUfoTimeUfoSpawned = 99999999;

    }

    private void runRandomWaveCenterUFO() {

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);

        if (!randomWaveCenterUfoSpawned) {

            //Spawn a UFO that stops in center and shoots beams in all directions for a specified
            //amount of time
            ufos.spawnStopInCenterUfo(ufos.ENERGY_BEAM_ALL_DIRECTIONS, 5);
            //Spawn a teleport that can be used to dodge the center UFO
            teleports.spawnTeleports();
            randomWaveCenterUfoSpawned = true;
        }


        checkIfRandomWaveIsComplete(RANDOM_WAVE_UFO_CENTER_TOTAL_TIME);

    }

    private void runRandomWaveVerticalUFO() {

        if (!ExclamationMark.notificationSpawned) {
            //Spawn a warning notification if it is not yet spawned ( for UFO coming from the top)
            ExclamationMark.spawnExclamationMark(Notifications.DIRECTION_TOP);
        }

        if (ExclamationMark.notificationIsComplete()) {
            //If notification has finished displaying,
            // Spawn a UFO coming from the top of the screen
            if (!randomWaveVertUfoSpawned) {

                //Spawn a UFO that travels vertically from top to bottom and shoots a straight vertical
                //line of energy beams (i.e. a top and bottom beam)
                ufos.spawnVerticalUfo(ufos.ENERGY_BEAM_VERTICAL_DIRECTIONS);
                randomWaveVertUfoSpawned = true;
            }
        }

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);


        checkIfRandomWaveIsComplete(RANDOM_WAVE_UFO_VERT_TOTAL_TIME);


    }

    private void runRandomWaveMissiles() {
        //Spawn both regular rockets and alien missiles

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);

        if (currentTimeInMillis - rockets.getLastRocketSpawnTime() > RANDOM_WAVE_MISSILES_SPAWN_DURATION) {
            rockets.spawnRocket();
        }
        if (currentTimeInMillis - alienMissiles.getLastAlienMissileSpawnTime() > RANDOM_WAVE_MISSILES_SPAWN_DURATION) {
            alienMissiles.spawnAlienMissile(alienMissiles.SPAWN_DIRECTION_LEFTWARD);
        }

        checkIfRandomWaveIsComplete(RANDOM_WAVE_MISSILES_TOTAL_TIME);

    }

    private void runRandomWaveMeteors() {

        if (!ExclamationMark.notificationSpawned) {
            //Spawn a warning notification if it is not yet spawned ( for meteors coming from the top)
            ExclamationMark.spawnExclamationMark(Notifications.DIRECTION_TOP);
        }

        if (ExclamationMark.notificationIsComplete()) {
            //If notification has finished displaying,
            // Spawn meteors coming from the top of the screen
            if (currentTimeInMillis - meteors.getLastMeteorSpawnTime() > RANDOM_WAVE_METEORS_SPAWN_DURATION) {
                //Spawn meteors
                meteors.spawnMeteor();
            }
        }

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);

        checkIfRandomWaveIsComplete(RANDOM_WAVE_METEORS_TOTAL_TIME);

    }

    private void runRandomWaveHorizontalUFO() {

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);

        if (!randomWaveHorizUfoSpawned) {

            //Spawn a UFO that travels horizontally from right to left and shoots a straight horizontal
            //line of energy beams (i.e. a left and right beam)
            ufos.spawnHorizontalUfo(ufos.ENERGY_BEAM_HORIZONAL_DIRECTIONS);
            randomWaveHorizUfoSpawned = true;
        }


        checkIfRandomWaveIsComplete(RANDOM_WAVE_UFO_HORIZ_TOTAL_TIME);

    }

    private void runRandomWaveVerticalUFOAndTeleport() {

        spawnBirds(RANDOM_WAVE_L1BIRD_SPAWN_DURATION, RANDOM_WAVE_L2BIRD_SPAWN_DURATION);

        if (!randomWaveVertTeleUfoSpawned) {

            //Spawn a UFO that travels horizontally from right to left and shoots a straight vertical
            //line of energy beams (i.e. a top and bottom beam) that must be jumped over using a teleport
            // The ufo stops at the right of the screen for 6 seconds to let the power beam generate before moving
            ufos.spawnStopInRightCenterUfo(ufos.ENERGY_BEAM_VERTICAL_DIRECTIONS, 6);
            randomWaveVerTeleUfoTimeUfoSpawned = currentTimeInMillis;
            randomWaveVertTeleUfoSpawned = true;

        }

        if (!randomWaveVertTeleUfoTeleportsSpawned && currentTimeInMillis - randomWaveVerTeleUfoTimeUfoSpawned > RANDOM_WAVE_VERT_UFO_TELEPORT_TIME_BEFORE_TELE_SPAWN) {
            //If time before when the teleport should spawn has passed and teleports haven't spawned yet
            // , spawn teleports that can be used by the player to dodge the energy beam
            teleports.spawnTeleports();
            teleports.spawnTeleports();
            randomWaveVertTeleUfoTeleportsSpawned = true;

        }


        checkIfRandomWaveIsComplete(RANDOM_WAVE_VERT_UFO_TELEPORT_TOTAL_TIME);


    }
}
