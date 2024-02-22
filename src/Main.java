public class Main {

    public static void main(String[] args){

        /*int[][] instances = new int[][]{
                {10,2,9},{12,2,11},{14,2,12},{16,2,10},
                {12,3,4},{15,3,6},{18,3,6},{21,3,6},{24,3,6},
                {16,4,5},{20,4,4},{24,4,4},{28,4,4},{32,4,3}
        };*/
        /*Design cruise;
        for(int[] instance : instances){
            cruise = new Design(instance[0], instance[1], instance[2]);
            cruise.model();
        }*/
        Design cruise;
        if (args.length != 3) {
            throw new IllegalArgumentException("Please provide exactly three arguments");
        }
        int d = Integer.parseInt(args[0]);
        int c = Integer.parseInt(args[1]);
        int e = Integer.parseInt(args[2]);
        cruise = new Design(d, c, e);
        cruise.model();
        System.out.println(cruise.getConstraints());

        /*
        Design cruise = new Design(8,2,7);
        System.out.println("tables is "+cruise.tables);

        cruise.model();
        System.out.println(cruise.nb_clauses+", actually "+cruise.actual_nb_clauses+" clauses");
        System.out.println(cruise.actual_nb_clauses_correspondence+" correspondence constraints "+ cruise.nbclause_correspondence_constraint * cruise.nb_correspondence_constraints);
        System.out.println(cruise.actual_nb_clauses_pair+" pair constraints "+ cruise.nbclause_pair_constraint * cruise.nb_pair_constraints);
        System.out.println(cruise.actual_nbclauses_sum+" sum constraints "+ (cruise.nbclause_col_constraint1 + cruise.nbclause_col_constraint2) * cruise.nb_col_constraints);

        Convertion conv = new Convertion();

        conv.showmat();
        */
    }

}