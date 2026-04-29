class ZooException extends Exception {
    public ZooException(String message) {
        super(message);
    }
}

class NotEnoughFoodException extends ZooException {

    public NotEnoughFoodException(String message) {
        super(message);
    }
}

class AnimalNotFoundException extends ZooException {

    public AnimalNotFoundException(String message) {
        super(message);
    }
}

class PersonNotFoundException extends ZooException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}

class UnauthorizedFeedException extends ZooException {

    public UnauthorizedFeedException(String message) {
        super(message);
    }
}
