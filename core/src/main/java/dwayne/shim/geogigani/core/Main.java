package dwayne.shim.geogigani.core;

import dwayne.shim.geogigani.core.storage.IdWeight;
import dwayne.shim.geogigani.core.storage.IdWeightStorage;

public class Main {

    public static void main(String[] args) throws Exception {
        IdWeightStorage ids = new IdWeightStorage();

        IdWeight iw = new IdWeight("1");
        iw.impress();
        iw.impress();
        ids.addIdWeight(iw);
        iw = new IdWeight("2");
        iw.click();
        ids.addIdWeight(iw);
        iw = new IdWeight("3");
        iw.impress();
        iw.click();
        ids.addIdWeight(iw);
        ids.sortList();
        System.out.println(ids.snapshot(2));
    }
}
