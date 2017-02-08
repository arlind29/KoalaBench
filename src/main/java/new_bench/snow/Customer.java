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
//import static java.util.Locale.ENGLISH;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.types.TpchMoney;

public class Customer extends AbstractEntity
{
    private final long customerKey;
    private final String name;
    private final String address;
    private final long nationKey;
    private final String phone;
    private final TpchMoney accountBalance;
    private final String marketSegment;
    public static String headers[] = {"customerKey","name","address","nationKey","phone","accountBalance","marketSegment"}; 
    public static String types[] = {"n","s","s","n","s","m","s", "s"}; 
    
    public Customer(long rowNumber, long customerKey, String name, String address, long nationKey, String phone, long accountBalance, String marketSegment)
    {
    	super(rowNumber);
    	this.relationName = "Customer"; 
    	String[] values = new String[7];
    
        values[0] = "" + (this.customerKey = customerKey);
        values[1] = this.name = checkNotNull(name, "name is null");
        values[2] = this.address = checkNotNull(address, "address is null");
        values[3] = "" + (this.nationKey = nationKey);
        values[4] = this.phone = checkNotNull(phone, "phone is null");
        values[5] = "" + (this.accountBalance = new TpchMoney(accountBalance));
        values[6] = this.marketSegment = checkNotNull(marketSegment, "marketSegment is null");
    	entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber(){ return rowNumber; }

    public long getCustomerKey(){ return customerKey; }

    public String getName(){ return name; }

    public String getAddress(){ return address; }

    public long getNationKey(){ return nationKey; }

    public String getPhone(){ return phone; }

    public double getAccountBalance(){ return accountBalance.getValue() / 100.0; }

    public long getAccountBalanceInCents(){ return accountBalance.getValue(); }

    public String getMarketSegment(){ return marketSegment; }

}
