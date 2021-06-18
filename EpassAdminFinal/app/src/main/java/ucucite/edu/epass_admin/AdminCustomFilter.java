package ucucite.edu.epass_admin;

import android.widget.Filter;

import java.util.ArrayList;

public class AdminCustomFilter extends Filter {

    AdminAdapter adminAdapter;
    ArrayList<Admintravellers> filterList;

    public AdminCustomFilter(ArrayList<Admintravellers> filterList, AdminAdapter adminAdapter)
    {
        this.adminAdapter = adminAdapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Admintravellers> filteredPets=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPets.add(filterList.get(i));
                }
            }

            results.count=filteredPets.size();
            results.values=filteredPets;

        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adminAdapter.persons = (ArrayList<Admintravellers>) results.values;

        //REFRESH
        adminAdapter.notifyDataSetChanged();

    }
}