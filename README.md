
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
```
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
```
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

To launch the application, use the command:

```
sbt run
```
This command will execute the main entry point of your application.

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
