package com.swiftant.afrisure.common;

import java.io.Serializable;
import java.util.ArrayList;

public class FlowDetails implements Serializable {
    public static final String FLOW_DETAILS = "flowDetails";

    public String lineOfBusiness;

    public String lineOfBusinessId;

    public ArrayList<String> products;

    public int[] productsId;

    public String quotationSearchID;

    public String quotationRefId;

    public FlowDetails() {

    }
}
