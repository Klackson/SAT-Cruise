import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class OutputConversion {

    public int[] outputvars;
    int[][][] intmatrix;
    StringBuilder final_matrix= new StringBuilder();
    int d,c,e,tables;

    public static void main(String[] args){
        if (args.length != 3) {
            throw new IllegalArgumentException("Please provide exactly three arguments");
        }
        int d = Integer.parseInt(args[0]);
        int c = Integer.parseInt(args[1]);
        int e = Integer.parseInt(args[2]);

        StringBuilder arg3= new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                arg3.append(line);
            }
        }
        catch (IOException error) {
            System.out.println("An error occurred in reading stdin.");
            error.printStackTrace();
        }

        OutputConversion conversion = new OutputConversion(arg3.toString(), d,c,e);
        System.out.println(conversion.getSolution());
    }

    public OutputConversion(String vars, int d, int c, int e){
        String[] listvars = vars.split("\\s+");
        int tables = d/c;

        //System.out.println("listvars range: 0 to " + (listvars.length - 1));
        //System.out.println("start index:  " + (d*d*e+1));
        //System.out.println("end index: " + (d*d*e+1 + d*tables*e));

        listvars = Arrays.copyOfRange(listvars, d*d*e+1, d*d*e+1 + d*tables*e);

        int[] intvars = new int[listvars.length];

        for(int i=0; i<listvars.length; i++){
            intvars[i] = Integer.valueOf(listvars[i]);
        }
        this.d=d;
        this.c=c;
        this.e=e;
        final_matrix.append(d).append(" ").append(c).append(" ").append(e).append("\n");
        tables = d/c;
        if(intvars.length != d*tables*e) outputvars = Arrays.copyOfRange(intvars, d*d*e+1, d*d*e+1 + d*tables*e);
        else outputvars = intvars;

        fillIntMatrix();
        fillStringMatrix();
    }

    public void fillIntMatrix(){
        intmatrix = new int[e][d][tables];
        int counter=0;
        for (int var : outputvars){
            intmatrix[counter/(d*tables)][(counter%(d*tables))/tables][counter%tables] = var;
            counter++;
        }
    }

    public void fillStringMatrix(){
        int diners_found;
        for(int t=0; t<e; t++){
            for(int col=0; col<tables; col++){
                diners_found=0;
                for(int i=0; i<d; i++){
                    if(Math.signum(intmatrix[t][i][col])>0) {
                        final_matrix.append(i + 1).append(" ");
                        diners_found++;
                        if(diners_found==2)break;
                    }
                }
                if(col!=tables-1) final_matrix.append(" ");
            }
            if(t!=e-1) final_matrix.append("\n");
        }
    }

    public String getSolution(){return final_matrix.toString();}

}
