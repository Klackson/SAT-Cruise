import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Design {

    //Problem parameters
    public int d, c, e, tables;

    public int nb_vars, nb_clauses;
    public int actual_nb_clauses =0, actual_nb_clauses_pair, actual_nb_clauses_correspondence, actual_nbclauses_sum;

    StringBuilder constraints = new StringBuilder();

    //Helper constants :
    final int nb_col_constraints, nb_pair_constraints, nb_correspondence_constraints;
    final int nbvar_per_col_constraint, nbvar_per_pair_constraint;
    final int nbclause_col_constraint, nbclause_pair_constraint, nbclause_correspondence_constraint;

    public Design(int d, int c, int e){
        this.e = e;
        this.c = c;
        this.d = d;
        this.tables = d/c;

        nb_col_constraints = tables*e*2;
        nb_pair_constraints = d*(d-1);
        nb_correspondence_constraints = d*(d-1)*tables*e;

        nbvar_per_col_constraint = (d-1)*c;
        nbvar_per_pair_constraint = e-1;

        this.nb_vars = d*d*e; // x variables
        this.nb_vars+= d*tables*e; // y variables
        this.nb_vars+= nb_col_constraints * nbvar_per_col_constraint; // s variables for column constraints
        this.nb_vars+= nb_pair_constraints * nbvar_per_pair_constraint; // s variables for pair constraints

        nbclause_pair_constraint = 2*e + e - 3 -1;
        nbclause_col_constraint = 2*d*c + d - 3*c -1;
        nbclause_correspondence_constraint = 2;

        this.nb_clauses = nbclause_pair_constraint * nb_pair_constraints
                + nbclause_col_constraint * nb_col_constraints
                + nbclause_correspondence_constraint * nb_correspondence_constraints;

        //this.constraints.append("p cnf ").append(nb_vars).append(" ").append(nb_clauses);
    }

    public void create_clause(List<Integer> variables){
        StringBuilder constraint = new StringBuilder();
        for(int variable : variables){
            constraint.append(variable).append(" ");
        }
        this.constraints.append("\n").append(constraint).append(" 0");
        actual_nb_clauses++;
    }

    public int vectorize_index(int[] indices, char vartype){
        if (vartype=='x') return indices[0] * d*d + indices[1] * d + indices[2];
        if (vartype=='y') return d*d*e + indices[0] * d*tables + indices[1] * tables + indices[2];
        else{
            System.out.println("Use a correct vartype");
            return 0;
        }
    }

    @Deprecated
    public void correspondence_constraint(){
        int v1, v2;
        ArrayList<Integer> vlist1, vlist2;
        for(int t=0; t<e; t++){
            for(int i=0; i<d; i++){
                for(int j=0; j<d; j++){
                    if(i==j) continue;
                    v1 = vectorize_index(new int[] {t,i,j}, 'x');
                    v2 = vectorize_index(new int[] {t,j,i}, 'x');

                    vlist1 = new ArrayList<>();
                    vlist1.add(v1);
                    vlist1.add(-v2);

                    vlist2 = new ArrayList<>();
                    vlist2.add(-v1);
                    vlist2.add(v2);

                    create_clause(vlist1);
                    create_clause(vlist2);
                }
            }
        }
    }

    public void solo_clause(int x1){
        ArrayList<Integer> clause = new ArrayList<>();
        clause.add(x1);
        create_clause(clause);
    }

    public void duo_clause(int x1, int x2){
        ArrayList<Integer> clause = new ArrayList<>();
        clause.add(x1);
        clause.add(x2);
        create_clause(clause);
    }

    public void trio_clause(int x1,int x2,int x3){
        ArrayList<Integer> clause = new ArrayList<>();
        clause.add(x1);
        clause.add(x2);
        clause.add(x3);
        create_clause(clause);
    }

    public void atmost_k(List<Integer> x_variables, int k, int constraint_number){
        int clausecount=0;

        int s_increment = d*d*e + d*tables*e;
        int n = x_variables.size();

        if (k==c){
            s_increment += nb_pair_constraints * nbvar_per_pair_constraint
                    + constraint_number * nbvar_per_col_constraint;
        }
        if(k==1){
            s_increment += constraint_number * nbvar_per_pair_constraint;
        }


        duo_clause(-x_variables.get(0), s_increment);
        clausecount++;

        for(int j=1; j<k; j++){
            solo_clause(-s_increment - j);
            clausecount++;

        }


        for (int i=1; i<n-1; i++){
            duo_clause(-x_variables.get(i), s_increment + i*k);
            clausecount++;


            duo_clause(-(s_increment + (i-1)*k), s_increment + i*k);
            clausecount++;


            for (int j=1; j<k; j++){
                trio_clause(-x_variables.get(i),
                        -(s_increment + (i-1)*k + j-1),
                        s_increment + i*k + j);
                clausecount++;


                duo_clause(-(s_increment + (i-1)*k + j), s_increment + i*k + j);
                clausecount++;

            }

            duo_clause(-x_variables.get(i), -(s_increment + (i-1)*k ));
            clausecount++;
        }

        duo_clause(-x_variables.get(n-1), s_increment + k*(n-1));
        clausecount++;

        //System.out.println(clausecount==2*n*k+n-3*k-1);
    }

    public void andimply(List<Integer> impliers, int implied){
        ArrayList<Integer> variables = new ArrayList<>();
        for (int implier : impliers){
            variables.add(-implier);
        }
        variables.add(implied);

        create_clause(variables);
    }

    public void orimply(List<Integer> impliers, int implied){
        ArrayList<Integer> variables;
        for (int implier : impliers){
            variables = new ArrayList<>();
            variables.add(-implier);
            variables.add(implied);

            create_clause(variables);
        }
    }

    public void xy_correspondence(){
        // Two persons at the same table imply they are eating together
        int count=0;
        int v1y, v2y, v1x, v2x;
        for(int t=0; t<e; t++){
            for(int l=0; l<tables; l++){
                for(int i=0; i<d; i++){
                    for(int j=0; j<d; j++){
                        if(i==j)continue;
                        v1y =vectorize_index(new int[]{t,i,l}, 'y');
                        v2y =vectorize_index(new int[]{t,j,l}, 'y');

                        v1x = vectorize_index(new int[]{t,i,j}, 'x');
                        v2x = vectorize_index(new int[]{t,j,i}, 'x');
                        andimply(new ArrayList<Integer>(List.of(v1y,v2y)), v1x);
                        andimply(new ArrayList<Integer>(List.of(v1y,v2y)), v2x);
                        count+=2;
                    }
                }
            }
        }
        actual_nb_clauses_correspondence = count;
    }

    public void c_per_table(){
        ArrayList<Integer> bounded_col, bounded_col_neg;
        int startpoint, endpoint;
        int constraint_number=0;

        for(int t=0; t<e; t++){
            for(int l=0; l<tables; l++){
                startpoint = vectorize_index(new int[]{t,0,l}, 'y');
                endpoint = vectorize_index(new int[]{t,d,l}, 'y');
                //System.out.println((endpoint-startpoint)/tables == d);

                bounded_col = new ArrayList<>();
                bounded_col_neg = new ArrayList<>();

                for(int i=startpoint; i<endpoint; i+=tables){
                    bounded_col.add(i);
                    bounded_col_neg.add(-i);
                }
                atmost_k(bounded_col, c, constraint_number);
                constraint_number++;
                atmost_k(bounded_col_neg, d-c, constraint_number);
                constraint_number++;
            }
        }
        actual_nbclauses_sum = constraint_number * nbclause_col_constraint;
    }

    public void no_double_dinner(){
        ArrayList<Integer> pairs_ij;
        int constraint_number=0;

        for(int i=0; i<d; i++){
            for(int j=0; j<d; j++){
                if(i==j) continue;
                pairs_ij = new ArrayList<>();

                for(int t=0; t<e; t++){
                    pairs_ij.add(vectorize_index(new int[]{t,i,j}, 'x'));
                }
                atmost_k(pairs_ij, 1, constraint_number);
                constraint_number++;
            }
        }
        actual_nb_clauses_pair = constraint_number * nbclause_pair_constraint;
    }

    public void solve(){
        no_double_dinner();
        c_per_table();
        xy_correspondence();

        //System.out.println(constraints);
        this.constraints.append("p cnf ").append(nb_vars).append(" ").append(actual_nb_clauses);

        try {
            String filename = "cruise"+d+"_"+c+"_"+e+".txt";
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(constraints.toString());
                myWriter.close();
            } else {
                System.out.println("File already exists.");
            }
            Stream<String> fileStream = Files.lines(Paths.get(filename));
            //Lines count
            System.out.println( fileStream.count()+" lines in the file");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
