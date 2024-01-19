
# Advice on buying in real estate project

This Scala-based project is an advanced real estate investment analysis tool designed to identify the best cities for investment based on user preferences and historical real estate sales data. Focusing on delivering personalized recommendations, it operates without a graphical user interface but through two robust APIs.

Key Features:
- Customization via API: Users can input their criteria such as budget, property type, and desired location through an API. These preferences are then used to tailor the recommendations.

- Data Processing with ZIO: The project employs ZIO, a powerful tool for asynchronous and concurrent programming in Scala, for data handling. This includes managing HTTP requests and performing tests, ensuring high performance and reliability.

- Integration of Historical Sales Data: By incorporating historical sales data, the system provides insights based on real market trends, leading to more accurate recommendations.

- HTTP Exposure: System functionalities are exposed through an HTTP interface, allowing easy integration with other systems or applications.

Use Case:
- This system is particularly useful for real estate investors and market analysts who require in-depth, personalized data analysis. It's also relevant for application developers looking to incorporate real estate analysis features into their own solutions.

Technologies Used:
- Programming Language: Scala
- ZIO for data processing, HTTP exposure, and testing
## API Reference

### Get all statistics

```http
  GET /v1/api/statistics
```
#### Request Body
```
None
```
#### Response Body
```json
[
  {
    "city": "Bourges",
    "data": {
      "averagePrices": [
        {
          "typeB": "Appartement",
          "averagePrice": 963050.0
        },
        {
          "typeB": "Maison",
          "averagePrice": 1010320.0
        },
        {
          "typeB": "Villa",
          "averagePrice": 1248775.0
        }
      ],
      "salesCounts": 30,
      "roomPriceAnalysis": [
        {
          "surface": 294.0,
          "numberOfRooms": 1,
          "averagePrice": 1548500.0
        },
        {
          "surface": 231.0,
          "numberOfRooms": 8,
          "averagePrice": 1252750.0
        },
        {
          "surface": 235.0,
          "numberOfRooms": 2,
          "averagePrice": 832500.0
        },
        {
          "surface": 231.0,
          "numberOfRooms": 5,
          "averagePrice": 1237750.0
        }
      ]
    }
  },
  {
    "city": "Nanterre",
    "data": {
      "averagePrices": [
        {
          "typeB": "Appartement",
          "averagePrice": 1070950.0
        },
        {
          "typeB": "Maison",
          "averagePrice": 943220.0
        },
        {
          "typeB": "Villa",
          "averagePrice": 1455125.0
        }
      ],
      "salesCounts": 30,
      "roomPriceAnalysis": [
        {
          "surface": 188.0,
          "numberOfRooms": 8,
          "averagePrice": 1027000.0
        },
        {
          "surface": 465.0,
          "numberOfRooms": 10,
          "averagePrice": 2491250.0
        },
        {
          "surface": 55.0,
          "numberOfRooms": 3,
          "averagePrice": 246000.0
        },
        {
          "surface": 255.0,
          "numberOfRooms": 8,
          "averagePrice": 932500.0
        }
      ]
    }
  },
]
```
#### Additional Details
These data represent the average purchase prices based on the number of rooms, the city, and the type of property.
### Get 3 top cities for buy

```http
  POST /v1/api/top-cities
```
#### Request Body


| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `budget`      | `double` | **Required**. Represents the user's budget for making their purchase |
| `typeOfHousing`      | `string` | **Required**. Represents the type of housing the user wishes to acquire. |
| `cities`      | `List[String]` | **Required**. Represents the cities where the user wishes to acquire a property |
| `numberOfRooms`      | `NumberOfRoomsInterval` | **Required**. Represents the number of bedrooms the user wants in their future home. |
| `surface`      | `SurfaceInterval` | **Required**. Represents the area the user desires in their future home. |
| `dataSince`      | `LocalDateTime` | **Required**. Represents the date on which the user wishes the API to analyze the sales data. |


#### Additional Details

The user must provide a minimum of four cities if they seek advice for specific cities. If they do not wish to choose any cities, then the city list must be left empty. The number of bedrooms is specified as a range, indicating the minimum and maximum number of bedrooms the user desires. The minimum must be more than 1, and the maximum should not exceed 99. Similarly, for the area, it is also specified as a range. The minimum area should be greater than 5 and less than 2000.

#### Response Body
```json
{
  "firstCity": {
    "city": "Montauban",
    "data": {
      "averagePrices": [
        {
          "typeB": "Maison",
          "averagePrice": 314520.0
        }
      ],
      "salesCounts": 5,
      "roomPriceAnalysis": [
        {
          "surface": 133.0,
          "numberOfRooms": 5,
          "averagePrice": 344200.0
        },
        {
          "surface": 104.0,
          "numberOfRooms": 10,
          "averagePrice": 299600.0
        }
      ]
    }
  },
  "secondCity": {
    "city": "Toulon",
    "data": {
      "averagePrices": [
        {
          "typeB": "Maison",
          "averagePrice": 364150.0
        }
      ],
      "salesCounts": 4,
      "roomPriceAnalysis": [
        {
          "surface": 60.0,
          "numberOfRooms": 2,
          "averagePrice": 262000.0
        }
      ]
    }
  },
  "thirdCity": {
    "city": "Versailles",
    "data": {
      "averagePrices": [
        {
          "typeB": "Maison",
          "averagePrice": 403457.14285714284
        }
      ],
      "salesCounts": 7,
      "roomPriceAnalysis": [
        {
          "surface": 31.0,
          "numberOfRooms": 6,
          "averagePrice": 160200.0
        }
      ]
    }
  }
}
```


#### Additional Details

These data represent the top three cities where the user could invest based on their criteria, along with the additional data that were used to perform the analysis.


## Architecture

For this project, we have adopted a clean architecture, implementing the principle of dependency inversion.

Our structure is composed of three main packages:

- `api`: Manages HTTP exposure.
- `domain`: Encapsulates business logic and models.
- `external`: Handles interactions with external data sources.
  The goal was to create a highly scalable application. Currently, we fetch sales data from CSV files, but in the future, this could be extended to retrieving data via an API or directly from a database.

The core functionalities of the application are based on traits representing use cases, such as `SaleAnalyzeUseCase`, which are implemented by services, like `SaleAnalyzeService`.

For data retrieval, we use a `SaleRepository` trait, implemented by the `SaleCsvData` class
## Prerequisites

Before getting started, ensure you have installed the following on your system:

Scala (version 3 or higher)
sbt (Scala Build Tool, version 1 or higher)
An IDE or text editor of your choice (recommended: IntelliJ IDEA, VSCode with the Scala extension)

## Cloning the Repository

Start by cloning the repository to your local machine using the following command:


```
git clone https://github.com/hermann93360/scala-school-project.git
```

## Compiling the Project

Open a terminal and navigate to the cloned project directory. Execute the following command to compile the project:

```
sbt compile
```
This command will download the necessary dependencies and compile the project.

## Running the Application

To launch our application, use the command or bellow if you are on Intellij run with launcher :

```
sbt run
```
This command will execute the main entry point of our application.

## Executing Tests

Our project uses the [ZIO Test](https://zio.dev/reference/test/) framework for unit testing. To run the tests, follow these steps:

1. Open a terminal and navigate to the project directory.
2. Execute the following command to start the tests:

```
sbt test
```
This command will run all the unit tests in the project and display the results in the terminal.

3. To run a specific test or a group of tests, you can use the following command:


```
sbt "testOnly *TestClassName"
```

Replace `TestClassName` with the name of the test class you want to run. For example, to only run tests in the `CriteriaSpec` class, use:


```
sbt "testOnly *CriteriaSpec"
```
## Test Reports

After running the tests, you can find the test reports in the `target/test-reports` directory. These reports provide useful details in case of test failures and can be used for debugging.

## Debugging Tips

If you encounter any issues during compilation or test execution, make sure that all dependencies are correctly installed and that your development environment is set up according to the prerequisites mentioned above.

For any issues encountered while running the project, please contact us on Teams or mail

*hermann.kamguin@efrei.net* and *franck.moungang@efrei.net*

## Overview of the `/v1/api/top-cities` API

### SaleAnalyzeController
This class exposes API endpoints, including one to retrieve the top three cities for investment. The request body is captured and converted into a `Criteria` object.

```scala
case req@Method.POST -> Root / "top-cities" =>
  req.body.asString.flatMap { bodyString =>
    ZIO.from(bodyString.fromJson[Criteria])
      .mapError(_ => new RuntimeException("Invalid data format"))
      .flatMap(checkAndGetTopCities)
      .map(_.toJson)
      .map(Response.json)
      .catchAll(handleException)
```

If the JSON format is invalid, an exception is raised. After constructing Criteria, a verification method is called to ensure the object complies with business rules.

```scala
private def checkAndGetTopCities(criteria: Criteria): ZIO[Any, Throwable, TopCities] =
  Criteria.check(criteria)
    .flatMap(_ => saleAnalyzeUseCase.findTopCities(criteria))
```

Any exceptions raised are handled by a handler and an appropriate response is returned.

```scala
private def handleException(e: Throwable): ZIO[Any, Nothing, Response] = e match {
  case e: CriteriaException =>
    ZIO.succeed(Response.json(ErrorResponse(Status.BadRequest.code, e.getMessage).toJson).withStatus(Status.BadRequest))
  case e: ClassNotFoundException =>
    ZIO.succeed(Response.json(ErrorResponse(Status.NotFound.code, e.getMessage).toJson).withStatus(Status.NotFound))
  case _ =>
    ZIO.succeed(Response.json(ErrorResponse(Status.InternalServerError.code, "Internal Server Error").toJson).withStatus(Status.InternalServerError))
}
```

### SaleAnalyzeService `findTopCities` Method


This method takes a Criteria object as a parameter and returns a ZIO effect producing a TopCities object. Sales data is filtered according to the provided criteria.

```scala
val filteredStream = filterSales(getStreamOfSaleData(saleRepository.getSaleData), criteria)
for {
  citySalesMap <- groupSalesByCity(filteredStream)
  cityStats <- ZIO.foreach(citySalesMap.toList) { case (city, sales) => calculateCityStatistics(city, sales) }
  topCities = cityStats
    .collect { case (cityStat, combinedAverage) if combinedAverage <= criteria.budget => (cityStat, combinedAverage) }
    .sortBy(_._2)
    .take(3)
} yield model.TopCities(
  topCities.headOption.map(_._1),
  topCities.lift(1).map(_._1),
  topCities.lift(2).map(_._1)
)
```
Sales are grouped by city, and statistics for each city are calculated. The top cities are selected based on the specified budget and combined average prices.

#### Filtering Sales
Sales are filtered based on criteria defined in the `Criteria` object.

```scala
private def filterSales(stream: ZStream[Any, Throwable, SaleData], filter: Criteria): ZStream[Any, Throwable, SaleData] =
  stream.filter { sale =>
    filter.city.forall(_.equals(sale.city)) &&
    sale.numberOfRooms >= filter.numberOfRooms.min &&
    sale.numberOfRooms <= filter.numberOfRooms.max &&
    sale.typeB == filter.typeB &&
    sale.area >= filter.surface.min &&
    sale.area <= filter.surface.max
  }
  ```

*Grouping Sales by City*

Sales are grouped by city to facilitate statistical analysis.

```scala
private def groupSalesByCity(stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, Map[City, List[SaleData]]] =
  stream.runFold(Map.empty[City, List[SaleData]])((
acc, sale) =>
acc.updated(sale.city, sale :: acc.getOrElse(sale.city, List.empty)))
```

#### Calculating Statistics by City
Statistics for each city are calculated, including average prices by property type and number of rooms.

```scala
private def calculateStatisticsByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, CityStatistics] = {
  for {
    averagePrices <- calculateAveragePrice(stream, city)
    salesCounts <- countSalesPerCity(stream, city)
    roomPriceAnalysis <- calculatePriceByNumberOfRooms(stream.filter(_.city == city), city)
  } yield CityStatistics(city, CityData(averagePrices, salesCounts, roomPriceAnalysis))
}
```

*Constructing the TopCities Object*

The TopCities object is constructed with the statistics of the top three cities. If fewer than three cities meet the criteria, the remaining slots are left empty (None).

#### Utilizing ZIO
ZIO is used for efficient handling of asynchronous operations and error management. It ensures non-blocking execution and robust error handling throughout the process of retrieving and analyzing the best cities for investment.

*Here we have explained the process and approach of the functions to retrieve the top three cities for investment. Feel free to take a look at the code to see it in its entirety.*
## Tests with ZIO


To successfully conduct our tests, we developed a class named `SaleDataStub`. The role of this class is to implement the `SaleRepository` trait. The objective is to inject it into the specific use case that we wish to test.

The `SaleDataStub` class is designed to contain fictitious data. These simulated data are used to represent the process of retrieving real data in a test environment. The advantage of this approach is twofold:

- **Test Isolation**: By using SaleDataStub, we isolate the use case from external dependencies, such as databases or web services. This ensures that the tests are reliable and do not depend on external factors that may vary.

- **Control Over Test Data**: By providing fictitious data, we have total control over the test scenarios. This allows for exhaustive and precise testing of the various logics and behaviors of the use case, without worrying about variations or unavailability of real data.

### Test Explanation for SaleServiceSpec
**SaleDataStub Implementation:**

- At the beginning of the test, we have a custom class `SaleDataStub` that implements `SaleRepository`. This is used to mock data retrieval in a controlled way, allowing for customized mocking of sales data.

```scala
val tested: SaleAnalyzeUseCase = SaleAnalyzeService.initWith(SaleDataStub())
```

**Data Preparation**

- The test uses `SaleDataStub.createSaleDateAndAdd` to simulate adding sales data for properties in various cities. This includes different properties in cities A, B, and C with varied attributes like price, number of rooms, and property type.
- By setting up mock data, the test ensures a diverse dataset for evaluating the `findTopCities` function.

```scala
_ <- ZIO.attempt {
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("A"), "Villa", 300, 5, 200000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("C"), "Villa", 300, 4, 100000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("C"), "Villa", 300, 4, 90000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("A"), "Villa", 300, 3, 150000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Villa", 300, 5, 250000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Villa", 300, 5, 400000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Maison", 300, 5, 200000, LocalDateTime.now)
        }
```

#### Test Criteria Definition :
- A Criteria object is defined with specific parameters: a budget of 500,000, property type "Villa", room number range from 1 to 6, and surface area between 20 to 600 square meters. This criteria is used to filter and determine the top cities.

```scala
criteria = Criteria(500000, "Villa", List.empty, NumberOfRoomsInterval(1, 6), SurfaceInterval(20, 600), LocalDateTime.now)

```

#### Test Execution and Assertions :

**Function Call**

- The test calls the findTopCities function from the tested service with the previously defined criteria. This function is expected to analyze the mock sales data and identify the top cities based on the criteria.

**Validation Through Assertions**

- The test validates the results with several assertions:
  - The result should be an instance of the TopCities class.
  - The top three cities (firstCity, secondCity, thirdCity) in the result should not be None.
  - The order of the top cities should match expectations based on the test setup and criteria: City "C" as the top city, followed by "A" and "B".

```scala
result <- tested.findTopCities(criteria)
_ <- ZIO.succeed {
  result shouldBe a[TopCities]
  result.firstCity should not be None
  result.secondCity should not be None
  result.thirdCity should not be None
  result.firstCity.get.city should be("C")
  result.secondCity.get.city should be("A")
  result.thirdCity.get.city should be("B")
}
```

#### Conclusion of the Test:
The test concludes with `yield assertCompletes`, signifying that the test is successful if all assertions pass without errors.

#### Purpose:
This test is designed to ensure the correct functionality of the `findTopCities` method. It checks that the method not only works as intended but also accurately reflects the expected results based on known input data. The use of `SaleDataStub` to mock `SaleRepository` allows for precise control over the test conditions, ensuring comprehensive and reliable testing of the service logic.