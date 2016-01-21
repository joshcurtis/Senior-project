**Home**
---
  Nothing yet

* **URL**

  /

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** `Hello World!`


* **Sample Call:**

  `/`


**Files**
---
  Get the available files for remote reading and writing.

* **URL**

  /files

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** `{files: ['a', 'b']}`


* **Sample Call:**

  `/files`

**Get File Contents**
---
  Retrieve the contents of a file.

* **URL**

  /files/:filename

* **Method:**

  `GET`

* **URL Params**

  **Required:**

  `filename=[string]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** `{
    file: 'test.py',
    data: 'print("Hello World!")'
  }`


* **Sample Call:**

  `/files/test.py`

**Write File Contents**
---
  Write the contents to a file.

* **URL**

  /files/:filename

* **Method:**

  `PUT`

* **URL Params**

  **Required:**

  `filename=[string]`

* **Data Params**

  `Text`

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** `{bytes_written:1024}`


* **Sample Call:**

  ```javascript
    $.ajax({
      url: "/files/test.py",
      dataType: "json",
      type: "PUT",
      data: "print('Hello World')",
      success: function(r) {
        console.log('Data successfully written.')
      }
    })
  ```

**Services**
---
  Get the services that can be started.

* **URL**

  /services

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** ```{
    services_are_running: True,
    services: [
      'python maintenance.py',
      'powersaver.sh'
    ]
  }
  ```


* **Sample Call:**

  `/services`

**Start Services**
---
  Start the services.

* **URL**

  /services/start

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** ```{
    services_are_running: True,
    services: [
      'python maintenance.py',
      'powersaver.sh'
    ]
  }
  ```


* **Sample Call:**

  `/services/start`


**Start Services**
---
  Stop the services.

* **URL**

  /services/stop

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** ```{
    services_are_running: False,
    services: [
      'python maintenance.py',
      'powersaver.sh'
    ]
  }
  ```


* **Sample Call:**

  `/services/stop`


**Reset Services**
---
  Stops the services and the starts them again.

* **URL**

  /services/reset

* **Method:**

  `GET`

* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br/>
  **Content:** ```{
    services_are_running: True,
    services: [
      'python maintenance.py',
      'powersaver.sh'
    ]
  }
  ```



* **Sample Call:**

  `/services/reset`
