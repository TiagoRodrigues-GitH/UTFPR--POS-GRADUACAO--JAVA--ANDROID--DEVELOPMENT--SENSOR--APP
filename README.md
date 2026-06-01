# SensorHub

**Android App for Field Sensor Data Collection and Monitoring**

<img width="282" height="217" alt="Screenshot 2026-05-11 142029" src="https://github.com/user-attachments/assets/a6f5f08b-b312-4e7f-b793-2b41dee85df3" />

---

## 📋 About the Project

SensorHub is an offline-first Android application developed for collecting sensor data in field environments without internet connectivity.

The app integrates three main data capture modules:

- **IoT Sensors**: Integration with external IoT devices

---

## 🎯 Key Features

- ✅ **MVVM architecture** with clear separation of concerns
- ✅ **Offline persistence** using Room Database
- ✅ **Asynchronous processing** via ExecutorService and WorkManager
- ✅ **Compatibility** with API 24+ and Nexus 7 devices

---

## 🏗️ Architecture
         
         SensorHub/
          ├── data/ # Data layer (Entities, DAOs, Database, Repositories)
          ├── domain/ # Business logic (Use Cases)
          ├── hardware/ # Physical sensor abstraction
          ├── ui/ # Presentation layer (Activities, ViewModels, Adapters)
          └── util/ # Utility classes

---

## 🛠️ Technologies

| Category | Technology |
|-----------|------------|
| **Language** | Java |
| **Database** | Room Persistence Library |
| **Architecture** | MVVM with LiveData and ViewModels |
| **UI** | ViewBinding, RecyclerView, Material Design |
| **Processing** | ExecutorService, WorkManager |
| **ML** | Pure Java implementations |

---

## 📱 Modules

### 1️⃣ IoT Sensors
- Management and data collection from external IoT sensors


---

## 📱 Screenshots

<img width="381" height="685" alt="Screenshot 2026-05-11 142010" src="https://github.com/user-attachments/assets/608e5381-8257-4109-a785-a9044a6884e9" />

<img width="381" height="685" alt="Screenshot 2026-05-11 140311" src="https://github.com/user-attachments/assets/0940645e-6479-4733-ba53-80a6b85b4733" />

<img width="381" height="685" alt="Screenshot 2026-05-11 141318" src="https://github.com/user-attachments/assets/23ced3ad-46a0-4433-a638-a85c1c5dad43" />

<img width="381" height="685" alt="Screenshot 2026-05-11 141129" src="https://github.com/user-attachments/assets/16395710-c5be-44ff-8607-d8a891210e0f" />

---

## 👨‍💻 Author

**Tiago Rodrigues**  
Postgraduate Program in Java Technologies - UTFPR  

---

## 📄 License

This project was developed as part of academic activities at the **Federal University of Technology – Paraná (UTFPR)**.

---

<div align="center">

**Academic Project** | UTFPR - Londrina Campus | 2026

</div>
