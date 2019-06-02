package grep;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


class Grep {
    @Option(name = "-r")
    boolean r; //REMINDER: FOLLOWS FILTERING CONDITIONS
    @Option(name = "-i")
    boolean i; //REMINDER: IGNORE WORD CASE
    @Option(name = "-v")
    boolean v; //REMINDER: INVERTS FILTERING CONDITIONS
    @Argument
    String word;
    @Argument(index = 1)
    String file;

    List<String> wordFilter(List<String> lines) {
        if (i) word = word.toLowerCase();
        List<String> output = new ArrayList<>();
        for(String line: lines) {
            boolean modifier = false;
            String currentLine = line;
            if (i)
                currentLine = currentLine.toLowerCase();
            String[] Words = currentLine.split("\\s+");
            for (String Word : Words) {
                if (word.toLowerCase().equals(Word)) modifier = true;
            }
            if ((v && !modifier) || (!v && modifier)) output.add(line);
        }
        return output;
    }

    List<String> optionChosen(List<String> lines) {
        List<String> output = new ArrayList<>();
        for(String line: lines){
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
        List<String> currentLines = new ArrayList<>();
        List<String> output;
        if (!(new File(input).exists()) || !(new File(input).isFile()))
            System.out.println("Incorrect input");
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(input))){
            reader.lines().forEach(currentLines::add);
        }
        if (r)
            output = optionChosen(currentLines);
        else
            output = wordFilter(currentLines);
        return output;
    }
}