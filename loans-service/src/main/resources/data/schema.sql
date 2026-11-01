CREATE TABLE IF NOT EXISTS `loans` (
    `loan_id` INT NOT NULL AUTO_INCREMENT,
    `mobile_number` VARCHAR(15) NOT NULL,
    `loan_number` VARCHAR(100) NOT NULL,
    `loan_type` VARCHAR(100) NOT NULL,
    `total_loan` INT NOT NULL,
    `paid_amount` INT NOT NULL,
    `outstanding_amount` INT NO NULL,
    `created_at` DATE NOT NULL,
    `created_by` VARCHAR(20) NOT NULL,
    `updated_at` DATE DEFAULT NULL,
    `updated_by` VARCHAR(20) DEFAULT NULL

    PRIMARY_KEY (`loan_id`)
);
