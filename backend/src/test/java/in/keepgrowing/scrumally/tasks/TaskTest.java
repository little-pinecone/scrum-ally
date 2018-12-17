package in.keepgrowing.scrumally.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class TaskTest {

    @Autowired
    private
    JacksonTester<Task> jacksonTester;

    @Test
    public void serializesTaskWithProject() throws IOException {
        Resource expectedJson = new ClassPathResource("serializedTask.json");

        assertThat(jacksonTester
                .write(getTask()))
                .isEqualToJson(expectedJson);
    }

    @Test
    public void deserializesTaskWithProject() throws IOException {
        assertThat(jacksonTester
                .parse(getSerializedTask()))
                .isEqualTo(getTask());
    }

    private String getSerializedTask() throws IOException {
        Resource pathResource = new ClassPathResource("serializedTask.json");
        return new String(Files.readAllBytes(pathResource.getFile().toPath()));
    }

    private Task getTask() {
        Task task = new Task("name", "");
        task.setProjectFromId(1L);
        return task;
    }
}