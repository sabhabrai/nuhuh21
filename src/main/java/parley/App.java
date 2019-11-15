/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package parley;

import parley.ecs.components.PhysicalObject;
import parley.ecs.components.Tag;
import parley.ecs.core.Engine;
import parley.systems.AIMovement;
import parley.systems.PlayerMovement;
import parley.systems.UI;

import java.util.ArrayDeque;
import java.util.Queue;


public class App {
    private static Queue<Integer> turnQueue = new ArrayDeque<>();

    public static void main(String[] args) throws InterruptedException {
        Engine engine = new Engine();

        int playerId = engine.newEntity()
                .withComponent(new PhysicalObject('@', 50, 11))
                .withTag(Tag.Player)
                .build();

        int ai1Id = engine.newEntity()
                .withComponent(new PhysicalObject('+', 40, 32))
                .withTag(Tag.AI)
                .build();

        int ai2Id = engine.newEntity()
                .withComponent(new PhysicalObject('-', 40, 32))
                .withTag(Tag.AI)
                .build();

        for (int i = 10; i <= 70; ++i) {
            engine.newEntity()
                    .withComponent(new PhysicalObject('#', i, 20))
                    .build();
        }

        Inputs.startService();
        turnQueue.add(playerId);
        turnQueue.add(ai1Id);
        turnQueue.add(ai2Id);

        runEventLoop(engine);
    }

    private static void runEventLoop(Engine engine) throws InterruptedException {
        PlayerMovement playerMovement = new PlayerMovement();
        AIMovement aiMovement = new AIMovement();
        UI ui = new UI();

        LoopState state = LoopState.NORMAL;

        while (true) {
            long startTime = System.currentTimeMillis();

            engine.runSystem(ui);

            int acting = turnQueue.peek();

            if (state == LoopState.NORMAL) {
                acting = turnQueue.poll();
                turnQueue.add(acting);

                engine.addTag(acting, Tag.Acting);

                if (engine.hasTags(acting, Tag.Player)) {
                    state = LoopState.PLAYER_MOVE;
                }
            }

            if (state == LoopState.PLAYER_MOVE) {
                engine.runSystem(playerMovement);

                if (playerMovement.isDone()) {
                    engine.removeTag(acting, Tag.Acting);
                    state = LoopState.NORMAL;
                }
            }
            else {
                engine.runSystem(aiMovement);
                engine.removeTag(acting, Tag.Acting);
            }

            long sleepTime = Math.max(0, 10 - (System.currentTimeMillis() - startTime));
            Thread.sleep(sleepTime);
        }
    }

    private enum LoopState {
        NORMAL, PLAYER_MOVE;
    }
}
