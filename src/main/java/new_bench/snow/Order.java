/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package new_bench.snow;

import static com.google.common.base.Preconditions.checkNotNull;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.types.TpchDate;
import new_bench.types.TpchMoney;

public class Order extends AbstractEntity{
    private final long orderKey;
    private final long customerKey;
    private final char orderStatus;
    private final TpchMoney totalPrice;
    private final TpchDate orderDate;
    private final String orderPriority;
    private final String clerk;
    private final long shipPriority;
    private final String comment;

    public static String[] headers= {"orderKey","customerKey","orderStatus","totalPrice","orderDate","orderPriority","clerk","shipPriority","comment"}; 
    public static String types[] = {"n","n","s","m","d","s","s","n","s"};

    public Order(long rowNumber,
            long orderKey,
            long customerKey,
            char orderStatus,
            long totalPrice,
            int orderDate,
            String orderPriority,
            String clerk,
            long shipPriority,
            String comment)
    {
    	super(rowNumber);
    	String[] values = new String[9];
    	this.relationName = "Order";

        values[0] = "" + (this.orderKey = orderKey);
        values[1] = "" + (this.customerKey = customerKey);
        values[2] = "" + (this.orderStatus = orderStatus);
        values[3] = "" + (this.totalPrice = new TpchMoney(totalPrice));
        values[4] = "" + (this.orderDate = new TpchDate(orderDate));
        values[5] = this.orderPriority = checkNotNull(orderPriority, "orderPriority is null");
        values[6] = this.clerk = checkNotNull(clerk, "clerk is null");
        values[7] = "" + (this.shipPriority = shipPriority);
        values[8] = this.comment = checkNotNull(comment, "comment is null");
        entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber(){   return rowNumber;    }

    public long getOrderKey(){   return orderKey;    }

    public long getCustomerKey(){   return customerKey;    }

    public char getOrderStatus(){   return orderStatus;    }

    public double getTotalPrice(){   return totalPrice.getValue() / 100.0;    }

    public long getTotalPriceInCents(){   return totalPrice.getValue();    }

    public int getOrderDate(){   return orderDate.getValue();    }

    public String getOrderPriority(){   return orderPriority;    }

    public String getClerk(){   return clerk;    }

    public long getShipPriority(){   return shipPriority;    }

    public String getComment(){   return comment;    }
    
    public static void main(String argz[]){
    	Order x = new Order(1,1,1,'a',1,9001,"a","b",4,"t");
    	System.out.println(x); 
    	System.out.println(x.toLine(null));
    	System.out.println(x.toJson(null));
    	System.out.println(x.toXML(null));
    	System.out.println(x.toCSV(",", null));
    }    
    

}
