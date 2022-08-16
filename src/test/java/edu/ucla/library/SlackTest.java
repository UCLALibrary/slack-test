
package edu.ucla.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.response.files.FilesUploadResponse;

@RunWith(JUnit4.class)
public class SlackTest {

    /**
     * A test CSV file.
     */
    private static final String CSV_FILE = "src/test/resources/test.csv";

    /**
     * The number of times the test loops.
     */
    private static final int LOOP_COUNT = 100;

    /**
     * A test of the Slack upload.
     */
    @Test
    public void testSlackUpload() {
        final String slackChannelID = System.getProperty(Config.SLACK_CHANNEL_ID);
        final String botToken = System.getProperty(Config.SLACK_OAUTH_TOKEN);
        final Slack slack = Slack.getInstance();

        for (int count = 0; count < LOOP_COUNT; count++) {
            try {
                final byte[] bytes = Files.readAllBytes(Path.of(CSV_FILE));
                final List<String> channels = Arrays.asList(new String[] { slackChannelID });
                final FilesUploadResponse response = slack.methods(botToken)
                        .filesUpload(post -> post.channels(channels).fileData(bytes).filename("slack-test.csv")
                                .filetype("csv").initialComment("This is a test upload").title("Slack-Test Upload"));

                if (response.isOk()) {
                    System.out.println("Success: #" + (count + 1));
                } else {
                    System.err.println(response.getError());
                }
            } catch (IOException | SlackApiException details) {
                System.err.println(details);
            }
        }
    }
}
