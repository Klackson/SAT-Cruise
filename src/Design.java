import java.util.*;

public class Design {

    //Problem parameters
    public int d, c, e, tables;

    public int nb_vars, nb_clauses;

    StringBuilder constraints = new StringBuilder();

    //Helper constants :
    final int nb_row_constraints, nb_pair_constraints, nb_symmetry_constraints;
    final int nbvar_per_row_constraint, nbvar_per_pair_constraint;
    final int nbclause_row_constraint, nbclause_pair_constraint, nbclause_symmetry_constraint;

    public Design(int d, int c, int e){
        this.e = e;
        this.c = c;
        this.d = d;
        this.tables = d/c;

        nb_row_constraints = d*e*2;
        nb_pair_constraints = (d*d-d)/2;
        nb_symmetry_constraints = d*(d-1)*e;
        nbvar_per_row_constraint = (d-1)*c;
        nbvar_per_pair_constraint = e-1;

        this.nb_vars = d*d*e; // x variables
        this.nb_vars+= nb_row_constraints * nbvar_per_row_constraint; // s variables for row constraints
        this.nb_vars+= nb_pair_constraints * nbvar_per_pair_constraint; // s variables for pair constraints

        nbclause_pair_constraint = 2*e + e - 3 -1;
        nbclause_row_constraint = 2*d*c + d - 3*c -1;
        nbclause_symmetry_constraint = 2;

        this.nb_clauses = nbclause_pair_constraint * nb_pair_constraints
                + nbclause_row_constraint * nb_row_constraints
                + nbclause_symmetry_constraint * nb_symmetry_constraints;

        this.constraints.append("p cnf ").append(nb_vars).append(" ").append(nb_clauses);
    }

    public void add_constraint(String constraint){
        this.constraints.append("\n").append(constraint).append(" 0");
    }

    public void create_constraint(List<Integer> variables){
        StringBuilder constraint = new StringBuilder();
        for(int variable : variables){
            constraint.append(variable).append(" ");
        }
        add_constraint(constraint.toString());
    }

    public int vectorize_index(int[] indices){
        return indices[0] * d*d + indices[1] + d + indices[2];
    }

    public void symmetry_constraint(){
        int v1, v2;
        ArrayList<Integer> vlist1, vlist2;
        for(int t=0; t<e; t++){
            for(int i=0; i<d; i++){
                for(int j=0; j<d; j++){
                    if(i==j) continue;
                    v1 = vectorize_index(new int[] {t,i,j});
                    v2 = vectorize_index(new int[] {t,j,i});

                    vlist1 = new ArrayList<>();
                    vlist1.add(v1);
                    vlist1.add(-v2);

                    vlist2 = new ArrayList<>();
                    vlist2.add(-v1);
                    vlist2.add(v2);

                    create_constraint(vlist1);
                    create_constraint(vlist2);
                }
            }
        }
    }

    public void atmost_k(List<Integer> x_variables, int k, int constraint_number){
        int s_increment = d*d*e + d*tables*e;
        int n = x_variables.size();

        if (k==c){
            s_increment += nb_pair_constraints * nbvar_per_pair_constraint
                    + constraint_number * nbvar_per_row_constraint;
        }
        if(k==1){
            s_increment += constraint_number * nbvar_per_pair_constraint;
        }


        ArrayList<Integer> clause = new ArrayList<>();

        clause.add(-x_variables.get(0));
        clause.add(s_increment);
        create_constraint(clause);

        for(int j=1; j<k; j++){
            clause = new ArrayList<>();
            clause.add(-s_increment - j);

            create_constraint(clause);
        }

        ////// Loop clauses \\\\\\\\
        for (int i=1; i<n-1; i++){
            clause = new ArrayList<>();
            clause.add(-x_variables.get(i));
            clause.add(s_increment + i*k);
            create_constraint(clause);

            clause = new ArrayList<>();
            clause.add(-(s_increment + (i-1)*k));
            clause.add(s_increment + i*k);
            create_constraint(clause);

            for (int j=1; j<k; j++){
                clause = new ArrayList<>();
                clause.add(-x_variables.get(i));
                clause.add(-(s_increment + (i-1)*k + j-1));
                clause.add(s_increment + i*k + j);
                create_constraint(clause);

                clause = new ArrayList<>();
                clause.add(-(s_increment + (i-1)*k + j));
                clause.add(s_increment + i*k + j);
                create_constraint(clause);
            }

            clause = new ArrayList<>();
            clause.add(-x_variables.get(i));
            clause.add(-(s_increment + (i-1)*k ));
            create_constraint(clause);
        }

        clause = new ArrayList<>();
        clause.add(-x_variables.get(n-1));
        clause.add(s_increment + k*(n-1) );
        create_constraint(clause);

    }

    public void andimply(List<Integer> impliers, int implied){
        ArrayList<Integer> variables = new ArrayList<>();
        for (int implier : impliers){
            variables.add(-implier);
        }
        variables.add(implied);

        create_constraint(variables);
    }

    public void orimply(List<Integer> impliers, int implied){
        ArrayList<Integer> variables;
        for (int implier : impliers){
            variables = new ArrayList<>();
            variables.add(-implier);
            variables.add(implied);

            create_constraint(variables);
        }
    }

}
