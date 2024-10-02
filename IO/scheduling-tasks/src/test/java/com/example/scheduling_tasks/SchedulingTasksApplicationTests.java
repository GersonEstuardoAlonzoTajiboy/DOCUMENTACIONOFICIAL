package com.example.scheduling_tasks;

import com.example.scheduling_tasks.task.ScheduledTask;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class SchedulingTasksApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpyBean
    ScheduledTask scheduledTask;

    @Test
    public void reportCurrentTime() {
        Awaitility.await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            Mockito.verify(scheduledTask, Mockito.atLeast(2)).reportCurrentTime();
        });
    }

}
