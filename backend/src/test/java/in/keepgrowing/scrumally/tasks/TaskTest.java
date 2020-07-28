package in.keepgrowing.scrumally.tasks;

import in.keepgrowing.scrumally.projects.model.Project;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.persistence.Id;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@JsonTest
class TaskTest {

    @Autowired
    private
    JacksonTester<Task> jacksonTester;

    @Test
    void serializesTaskWithProject() throws IOException {
        Resource expectedJson = new ClassPathResource("serializedTask.json");
        JsonContent<Task> parsed = jacksonTester.write(getTask());

        assertThat(parsed).isEqualToJson(expectedJson);
    }

    @Test
    void deserializesTaskWithProject() throws IOException {
        Task expected = getTask();
        Task parsed = jacksonTester.parse(getSerializedTask()).getObject();
        assertEquals(expected, parsed);
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