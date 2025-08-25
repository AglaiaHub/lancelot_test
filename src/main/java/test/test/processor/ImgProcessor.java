package test.test.processor;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;
import test.test.model.ListTasks;
import test.test.model.FileType;

@Log4j2
@Service("imgProcessor")
@AllArgsConstructor
public class ImgProcessor implements FileProcessor {

    private final TextractClient textractClient;


    @Override
    public ListTasks processFile(String fileUrl) {
        log.info("Processing img file");

        String string = extractTextFromImage(downloadImage(fileUrl));
        System.out.println(string);

        return new ListTasks().builder()
                .fileName(getNameFromUrl(fileUrl))
                .tasksList(getTaskDtosFromString(string))
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.IMG;
    }

    private byte[] downloadImage(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
        return imageBytes;
    }

    public String extractTextFromImage(byte[] imageBytes) {
        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                .document(Document.builder().bytes(SdkBytes.fromByteArray(imageBytes)).build())
                .build();

        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

        StringBuilder fullText = new StringBuilder();
        for (Block block : response.blocks()) {
            if (block.blockType() == BlockType.LINE) {
                fullText.append(block.text()).append("\n");
            }
        }

        return fullText.toString();
    }


}
