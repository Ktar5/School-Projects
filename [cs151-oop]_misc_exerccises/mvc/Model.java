

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<String> linesOfText = new ArrayList<>();

    public List<String> getLinesOfText() {
        return linesOfText;
    }

    public void addNewLine(String line) {
        linesOfText.add(line);
    }

}
