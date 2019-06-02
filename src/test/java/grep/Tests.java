package grep;

import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Tests {
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream error = new ByteArrayOutputStream();
    private String nextLine = System.lineSeparator();

    @Test
    void emptyCheck() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] empty = {};
        Execute.main(empty);
        assertEquals(output.toString(), "grep [-v] [-i] [-r] word inputname.txt" + nextLine);
    }
    @Test
    void nonexistentFile() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"abc", "undefined"};
        Execute.main(input);
        assertEquals(error.toString(),"undefined (Не удается найти указанный файл)" + nextLine);
    }

    @Test
    void noInput() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(), "grep [-v] [-i] [-r] word inputname.txt" + nextLine);
    }

    @Test
    void incorrectInput() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"life", "knife", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(error.toString(),
                "Too many arguments: src/samples/input.txt" + nextLine);
    }

    @Test
    void checkOne() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"wife", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(), "the life of a wife is ended by the knife" + nextLine);
    }

    @Test
    void parse() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"-r", "(\\S*\\s*)*(v)(\\S*\\s*)*", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(), "tHE life ov a wive is enedd by theknive" + nextLine);
    }

    @Test
    void ignoreCase() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"-i", "The", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(),
                "the life of a wife is ended by the knife" + nextLine +
                        "the lifeofaWi feisen dedbyth eknife" + nextLine +
                        "The Life Of A Wife Is Ended By The Knife" + nextLine +
                        "tHE life ov a wive is enedd by theknive" + nextLine);
    }

    @Test
    void invert() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"-v", "feofa", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(),
                "the life of a wife is ended by the knife" + nextLine +
                "the lifeofaWi feisen dedbyth eknife" + nextLine +
                "tHeLiFe OfAwIfEiSeNdE dByThEk NiFe" + nextLine +
                "The Life Of A Wife Is Ended By The Knife" + nextLine +
                "tHE life ov a wive is enedd by theknive" + nextLine);
    }

    @Test
    void invertAndParse() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
        String[] input = {"-v", "-r", "(\\S*\\s*)*(v)(\\S*\\s*)*", "src/samples/input.txt"};
        Execute.main(input);
        assertEquals(output.toString(),
                "the life of a wife is ended by the knife" + nextLine +
                        "Theli feofa Wifeise ndedby thekni fe" + nextLine +
                        "the lifeofaWi feisen dedbyth eknife" + nextLine +
                        "tHeLiFe OfAwIfEiSeNdE dByThEk NiFe" + nextLine +
                        "The Life Of A Wife Is Ended By The Knife" + nextLine);
    }
}