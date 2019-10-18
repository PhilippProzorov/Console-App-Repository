package grep;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import java.io.*;
import java.util.*;
import java.util.regex.*;


class Grep {
    @Option(name = "-r")
    private boolean r; //REMINDER: FOLLOWS FILTERING CONDITIONS
    @Option(name = "-i")
    private boolean i; //REMINDER: IGNORE WORD CASE
    @Option(name = "-v")
    private boolean v; //REMINDER: INVERTS FILTERING CONDITIONS
    @Argument
    private String word;
    @Argument(index = 1)
    private String file;

    private List<String> wordFilter(List<String> lines) {
        if (i) word = word.toLowerCase();
        List<String> output = new LinkedList<>();
        for(String line: lines) {
            boolean modifier = false;
            String currentLine = line;
            if (i)
                currentLine = currentLine.toLowerCase();
            String[] words = currentLine.split("\\s+");
            for (String word : words) {
                if (this.word.toLowerCase().equals(word)) {
                    modifier = true;
                    break;
                }
            }
            if ((v && !modifier) || (!v && modifier)) output.add(line);
        }
        return output;
    }

    private List<String> optionChosen(List<String> lines) {
        List<String> output = new LinkedList<>();
        for(String line: lines) {
            boolean aMatch = Pattern.compile(word).matcher(line).matches();
            if (!v) {
                if (aMatch)
                    output.add(line);
            } else {
                if (!aMatch)
                    output.add(line);
            }
        }
        return output;
    }

    List<String> getOutput() throws IOException {
        String input = file;
        final boolean nonexistent = !(new File(input).exists());
        final boolean notAFile = !(new File(input).isFile());
        List<String> currentLines = new LinkedList<>();
        List<String> output;
        if (nonexistent || notAFile)
            System.out.println("Incorrect input");
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(input))) {
            reader.lines().forEach(currentLines::add);
        }
        if (r)
            output = optionChosen(currentLines);
        else
            output = wordFilter(currentLines);
        return output;
    }
}
