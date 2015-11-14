package cn.ieclipse.af.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamling
 * 
 */
public class ProcessUtil {

    public static Process exec(String program, List<String> args) {
        ProcessBuilder builder = new ProcessBuilder();
        File f = new File(program);
        builder.directory(f.getParentFile());

        String cmd = String.format("\"%s\"", f.getName());
        if (args != null) {
            args.add(0, cmd);
            builder.command(args);
        } else {
            builder.command(cmd);
        }
        try {
            Process p = builder.start();
            return p;
        } catch (IOException e) {
            return null;
        }
    }

    public static Process exec(String program, String... args) {
        if (args != null) {
            List<String> array = new ArrayList<String>(args.length);
            for (int i = 0; i < args.length; i++) {
                array.add(args[i]);
            }
            return exec(program, array);
        }
        return exec(program, (List<String>) null);
    }

    public static Process exec(String program) {
        return exec(program, (List<String>) null);
    }

    public static ArrayList<String> run(String shell, String[] commands) {
        ArrayList<String> output = new ArrayList<String>();

        try {
            Process process = Runtime.getRuntime().exec(shell);

            BufferedOutputStream shellInput = new BufferedOutputStream(
                    process.getOutputStream());
            BufferedReader shellOutput = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            for (String command : commands) {
                shellInput.write((command + " 2>&1\n").getBytes());
            }

            shellInput.write("exit\n".getBytes());
            shellInput.flush();

            String line;
            while ((line = shellOutput.readLine()) != null) {
                output.add(line);
            }

            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }
}
