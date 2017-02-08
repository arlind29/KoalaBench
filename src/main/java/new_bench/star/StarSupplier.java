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
package new_bench.star;

import static com.google.common.base.Preconditions.checkNotNull;
import new_bench.snow.Supplier;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.types.TpchMoney;

public class StarSupplier extends AbstractEntity{
    private final long supplierKey;
    private final String name;
    private final String address;
    private final String nationName;
    private final String regionName;
    private final String phone;
    private final TpchMoney accountBalance;
    private final String comment;

    public static String[] headers = {"supplierKey","name","address","nationName","regionName","phone","accountBalance","comment"};   
    public static String types[] = {"n","s","s","s","s","s", "m", "s"};
    
    public StarSupplier(long rowNumber, long supplierKey, String name, String address, String nationName, String regionName, String phone, long accountBalance, String comment)
    {
    	super(rowNumber);
    	String[] values = new String[8];
    	this.relationName = "Supplier";

    	values[0] = "" + (this.supplierKey = supplierKey);
    	values[1] = this.name = checkNotNull(name, "name is null");
    	values[2] = this.address = checkNotNull(address, "address is null");
    	values[3] = this.nationName = nationName;
    	values[4] = this.regionName = regionName;
    	values[5] = this.phone = checkNotNull(phone, "phone is null");
    	values[6] = "" + (this.accountBalance = new TpchMoney(accountBalance));
    	values[7] = this.comment = checkNotNull(comment, "comment is null");
    	entity = new EntityInstance(relationName, headers, types, values); 
    }
    
    public StarSupplier(Supplier supplier, String nationName, String regionName){
    	this(supplier.getRowNumber(), supplier.getSupplierKey(), supplier.getName(), supplier.getAddress(), nationName, regionName, supplier.getPhone(), supplier.getAccountBalanceInCents(), supplier.getComment() );
    }

    @Override
    public long getRowNumber(){return rowNumber;  }

    public long getSupplierKey(){return supplierKey;}

    public String getName(){return name;}

    public String getAddress(){return address;}

    public String getNationName(){return nationName;}
    
    public String getRegionName(){return regionName;}

    public String getPhone(){return phone;}

    public double getAccountBalance(){return accountBalance.getValue() / 100.0; }

    public long getAccountBalanceInCents(){return accountBalance.getValue();}

    public String getComment(){return comment;}

}
