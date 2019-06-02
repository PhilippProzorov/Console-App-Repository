package grep;

import org.kohsuke.args4j.CmdLineParser;
import java.util.List;

class Execute {
    static int main(String[] input) {
        if ((input.length == 0) || (input.length == 1)) {
            System.out.println("grep [-v] [-i] [-r] word inputname.txt");
            return 0;
        }
        Grep grep = new Grep();
        CmdLineParser parser = new CmdLineParser(grep);
        try {
            parser.parseArgument(input);
            List<String> lines = grep.getOutput();
            for (String line : lines)
                System.out.println(line);
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}