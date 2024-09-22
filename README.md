Data generation prompts:

### Account.Customer: 
Generate 60 customer info in the format of Id (Starting from 1), AccountType (Always: CUSTOMER), Name, ContactNumber (like: 23457890, please try to make each number of it random!), Location (like: [35 45], please make it random, the value range is within -3000 to +3000!), CustomerType (1 or 2), Gender, Email.

### Account.Restaurant:
Generate 10 restaurant info in the format of Id (Starting from 11), AccountType (Always: RESTAURANT), Name, ContactNumber (like: 23457890, please try to make each number of it random!), Location (like: [35 45], please make it random, the value range is within -3000 to +3000!), District (Whatever in the world), Street (Whatever in the world).

### Account.Rider:
Generate 10 rider info in the format of Id (Starting from 11), AccountType (Always: RIDER), Name, ContactNumber (like: 23457890, please try to make each number of it random!), Location (like: [35 45], please make it random, the value range is within -3000 to +3000!), Gender, Status (The value is 3, 4, or 5, make it 4 more), UserRating (The type is double, range in 1 to 10), MonthTaskCount (The range is from 1 to 400, make it random!).

### Dish:
Generate 40 dish info in the format of Id (Starting from 1), Name, Desc (like: "A traditional Mexican dish", try to use a single sentence with least punctuations, "" included), Price (The range from 1 to 1000), RestaurantId (The range is 11 to 20, try to make the assignment even consecutive, like the first four dishes belong to restaurant 1, the next four belong to restaurant 2).

### Order:
Generate 50 order info in the format of Id, Status (The value is 6 or 8, make it 6 more, but leave about 30 percent to 8, remember when assigning 6, the riderId should be NA, when assigning 8 the riderId should be numbers), RestaurantId (The range is from 41 to 80, try to make it grow sequentially!), CustomerId (The range is from 1 to 40, try to make it random!), CreateTime (The range is from 0 to 3600, try to make it random!), IsPayed (0 or 1, make it 0 mostly, but do leave some 1.), OrderedDishes (Like [1 2 3 4], try to make the array size less equal to 4, and elements is in a range of x to x+4 for x is 1 plus the multiple of 4 (x = 4i + 1)), RiderId (NA or range from 81 to 120.).
