/**
 * Represents a person with basic identity and academic information.
 */
interface IPerson {
    /**
     * Returns the unique identifier of the person.
     *
     * @return person ID
     */
    public String getID();

    /**
     * Returns the full name of the person.
     *
     * @return person name
     */
    public String getName();

    /**
     * Returns the email address of the person.
     *
     * @return person email
     */
    public String getEmail();

    /**
     * Returns the academic major of the person.
     *
     * @return major enum value
     */
    public Major getMajor();
}

/**
 * Defines common properties for academic entities.
 */
interface IAcademicEntity {
    /**
     * Returns the code that uniquely identifies the entity.
     *
     * @return entity code
     */
    public String getCode();

    /**
     * Returns the name of the academic entity.
     *
     * @return entity name
     */
    public String getName();

    /**
     * Returns a brief description of the entity.
     *
     * @return entity description
     */
    public String getDescription();
}

/**
 * Provides a report-generation capability.
 */
interface IReportable {
    /**
     * Generates a text report summarizing the object's state.
     *
     * @return report string
     */
    public String generateReport();
}

