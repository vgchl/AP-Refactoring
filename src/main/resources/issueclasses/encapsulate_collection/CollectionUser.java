package issueclasses.encapsulate_collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionUser {

    public CollectionUser(CollectionHolder holder) {
        Object object = new Object();

        holder.objectList.add(object); // holver.addObjectList(object)
        holder.objectList.remove(object);

        List holderList = holder.objectList;
        holderList.add(object);
        holderList.remove(object);

        List myList = new ArrayList();
        myList.add(object);
        myList.remove(object);
    }

}
