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
import new_bench.snow.Customer;
//import static java.util.Locale.ENGLISH;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.types.TpchMoney;

public class StarCustomer extends AbstractEntity
{
    private final long customerKey;
    private final String name;
    private final String address;
    private final String nationName;
    private final String regionName;
    private final String phone;
    private final TpchMoney accountBalance;
    private final String marketSegment;
    public static String headers[] = {"customerKey","name","address","nation_name","regionName","phone","accountBalance","marketSegment"}; 
    public static String types[] = {"n","s","s","s", "s", "s","m","s", "s"}; 
    
    public StarCustomer(long rowNumber, long customerKey, String name, String address, String nationName, String regionName, String phone, long accountBalance, String marketSegment)
    {
    	super(rowNumber);
    	this.relationName = "Customer"; 
    	String[] values = new String[8];
    
        values[0] = "" + (this.customerKey = customerKey);
        values[1] = this.name = checkNotNull(name, "name is null");
        values[2] = this.address = checkNotNull(address, "address is null");
        values[3] = this.nationName = nationName;
        values[4] = this.regionName = regionName;
        values[5] = this.phone = checkNotNull(phone, "phone is null");
        values[6] = "" + (this.accountBalance = new TpchMoney(accountBalance));
        values[7] = this.marketSegment = checkNotNull(marketSegment, "marketSegment is null");
    	entity = new EntityInstance(relationName, headers, types, values); 
    }

    public StarCustomer(Customer cust, String nationName, String regionName){
    	this(cust.getRowNumber(), cust.getCustomerKey(), cust.getName(), cust.getAddress(), nationName, regionName, cust.getPhone(), cust.getAccountBalanceInCents(), cust.getMarketSegment()); 
    }
    
    @Override
    public long getRowNumber(){ return rowNumber; }

    public long getCustomerKey(){ return customerKey; }

    public String getName(){ return name; }

    public String getAddress(){ return address; }

    public String getNationName(){ return nationName; }
    public String getRegionName(){ return regionName; }

    public String getPhone(){ return phone; }

    public double getAccountBalance(){ return accountBalance.getValue() / 100.0; }

    public long getAccountBalanceInCents(){ return accountBalance.getValue(); }

    public String getMarketSegment(){ return marketSegment; }

}
