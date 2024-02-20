public class Main {

    public static void main(String[] args){
        Design cruise = new Design(32,4,3);

        cruise.solve();
        System.out.println(cruise.nb_clauses+", actually "+cruise.actual_nb_clauses+" clauses");
        System.out.println(cruise.actual_nb_clauses_correspondence+" correspondence constraints "+ cruise.nbclause_correspondence_constraint * cruise.nb_correspondence_constraints);
        System.out.println(cruise.actual_nb_clauses_pair+" pair constraints "+ cruise.nbclause_pair_constraint * cruise.nb_pair_constraints);
        System.out.println(cruise.actual_nbclauses_sum+" sum constraints "+ cruise.nbclause_col_constraint * cruise.nb_col_constraints);

    }

}