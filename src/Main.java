public class Main {

    public static void main(String[] args){
        Design cruise = new Design(8,2,7);
        System.out.println("tables is "+cruise.tables);

        cruise.solve();
        System.out.println(cruise.nb_clauses+", actually "+cruise.actual_nb_clauses+" clauses");
        System.out.println(cruise.actual_nb_clauses_correspondence+" correspondence constraints "+ cruise.nbclause_correspondence_constraint * cruise.nb_correspondence_constraints);
        System.out.println(cruise.actual_nb_clauses_pair+" pair constraints "+ cruise.nbclause_pair_constraint * cruise.nb_pair_constraints);
        System.out.println(cruise.actual_nbclauses_sum+" sum constraints "+ (cruise.nbclause_col_constraint1 + cruise.nbclause_col_constraint2) * cruise.nb_col_constraints);

    }

}