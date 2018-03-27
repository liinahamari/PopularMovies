/*
package com.example.guest.popularmovies.utils.pagination;

import com.example.guest.popularmovies.db.MoviesDbHelper;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

*/
/**
 * Created by l1maginaire on 3/27/18.
 *//*


public class Mapper {
    @Inject
    public Mapper() {
    }

    public List<SingleMovie> mapCakes(MoviesDbHelper storage, MoviesArray response) {
        List<SingleMovie> cakeList = new ArrayList<>();

        if (response != null) {
            List<SingleMovie> list = response.getCakes();
            if (list != null) {
                for (CakesResponseCakes cake : list) {
                    Cake myCake = new Cake();
                    myCake.setId(cake.getId());
                    myCake.setTitle(cake.getTitle());
                    myCake.setDetailDescription(cake.getDetailDescription());
                    myCake.setPreviewDescription(cake.getPreviewDescription());
                    myCake.setImageUrl(cake.getImage());
                    storage.addCake(myCake);
                    cakeList.add(myCake);
                }
            }
        }
        return cakeList;
    }
}
*/
