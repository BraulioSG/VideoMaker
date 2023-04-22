package utils;

public class ProgressBar {
    public static void displayProgressBar(int progress, int total) {
        StringBuilder progressBar = new StringBuilder();
        progressBar.append("[");

        int percent = (int)(((double)progress / ((double)total)) * (double) 100);
        if(percent > 100) percent = 100;

        int numSquares = percent / 2;
        for(int i = 1; i <= 50; i++){
            progressBar.append((i <= numSquares) ? "#": "-");
        }

        progressBar.append(String.format("] %d", percent));

        System.out.print(progressBar + "% " + (percent == 100 ? "done \n" : "\r"));
    }
}
