--SUPER ADMIN--
INSERT INTO employee(
	id, created_at, flyerssoft_id, designation, email, image, location, mobile_number, modified_at, employee_name, primary_manager_id, secondary_manager_id)
	VALUES (1, CURRENT_TIMESTAMP, 'superAdmin', 'Super Admin', 'master.account@flyerssoft.com', null, 'chennai', null, null, 'superAdmin', null, null);
