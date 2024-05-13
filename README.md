# Image Grid App

The Image Grid App is an Android application that allows users to view images in a scrollable grid fetched from an API. It efficiently loads and displays images in a 3-column square grid format.

## Features

- Display images in a 3-column square grid format.
- Asynchronously load images from an API endpoint.
- Implement caching mechanism for efficient retrieval of images.
- Gracefully handle network errors and image loading failures.
- Dynamically change the limit of images fetched from the API.
- Load more images as the user scrolls through the grid.
- Listen for real-time changes in internet connectivity.

## Technologies Used

- Kotlin
- Android Architecture Components (ViewModel, LiveData)
- RecyclerView
- Retrofit for network requests
- no third-party library allowed for image loading
- ConnectivityManager for monitoring network connectivity changes

## Installation

To run the Image Grid App on your local machine, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/akashydv04/Asignment_Acharya_Prashant.git
   ```

2. Open the project in Android Studio.

3. Build and run the project on an emulator or a physical device.

## Usage

1. Upon launching the app, you will see a grid of images.
2. Scroll through the grid to view more images.
3. The app dynamically loads more images as you scroll.
4. If you lose internet connectivity, the app will display a message indicating the lack of active internet connection.
5. Once the internet connection is restored, the app will resume loading images.

## Contributing

Contributions are welcome! If you'd like to contribute to the project, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/new-feature`).
3. Make your changes and commit them (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Create a new pull request.

## License

This project is licensed under the [MIT License](LICENSE).

---

Feel free to customize this README template to fit the specific details and requirements of your Image Grid App project.
