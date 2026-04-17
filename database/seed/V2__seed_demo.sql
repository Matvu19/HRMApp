INSERT INTO org_unit (org_unit_id, org_code, org_name, org_level, cost_center, location_code, status, created_by, updated_by)
VALUES
(1, 'ORG-HQ', 'Head Office', 1, 'CC100', 'HCM', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'ORG-ENG', 'Engineering', 2, 'CC200', 'HCM', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 'ORG-HR', 'Human Resources', 2, 'CC300', 'HCM', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(4, 'ORG-PAY', 'Payroll', 2, 'CC400', 'HCM', 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO position (position_id, position_code, position_name, job_family, grade_band, managerial_flag, status, created_by, updated_by)
VALUES
(1, 'POS-EMP', 'Employee', 'General', 'G1', FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'POS-MGR', 'Manager', 'Management', 'M1', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 'POS-HR', 'HR Specialist', 'HR', 'H1', FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(4, 'POS-PAY', 'Payroll Specialist', 'Finance', 'P1', FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(5, 'POS-ADM', 'System Admin', 'IT', 'A1', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO employee_profile (
    employee_id, employee_code, full_name, dob, gender, work_email, personal_phone,
    org_unit_id, position_id, manager_employee_id, join_date, status, created_by, updated_by
)
VALUES
(1, 'E001', 'Employee One', '1998-01-10', 'MALE', 'employee01@hrmapp.local', '0900000001', 2, 1, 2, '2024-01-02', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'M001', 'Manager One', '1990-05-11', 'MALE', 'manager01@hrmapp.local', '0900000002', 2, 2, NULL, '2020-03-01', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 'H001', 'HR One', '1992-06-15', 'FEMALE', 'hr01@hrmapp.local', '0900000003', 3, 3, NULL, '2021-04-01', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(4, 'P001', 'Payroll One', '1991-02-20', 'FEMALE', 'payroll01@hrmapp.local', '0900000004', 4, 4, NULL, '2021-04-01', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(5, 'A001', 'Admin One', '1989-09-09', 'MALE', 'admin01@hrmapp.local', '0900000005', 1, 5, NULL, '2019-01-01', 'ACTIVE', 'SYSTEM', 'SYSTEM');

UPDATE org_unit SET head_employee_id = 5 WHERE org_unit_id = 1;
UPDATE org_unit SET head_employee_id = 2 WHERE org_unit_id = 2;
UPDATE org_unit SET head_employee_id = 3 WHERE org_unit_id = 3;
UPDATE org_unit SET head_employee_id = 4 WHERE org_unit_id = 4;

INSERT INTO role (role_id, role_code, role_name, scope_type, is_system_role, status, created_by, updated_by)
VALUES
(1, 'EMPLOYEE', 'Employee', 'SELF', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'MANAGER', 'Manager', 'TEAM', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 'HR', 'HR', 'ORG', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(4, 'PAYROLL', 'Payroll', 'ORG', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(5, 'ADMIN', 'Admin', 'GLOBAL', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO user_account (
    user_id, employee_id, username, password_hash, auth_provider, mfa_enabled, biometric_enabled,
    status, created_by, updated_by
)
VALUES
(1, 1, 'employee01', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6I0qQ2V3Y8ExN1GpStO6Qf8Q4gkM2', 'LOCAL', FALSE, FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 2, 'manager01',  '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6I0qQ2V3Y8ExN1GpStO6Qf8Q4gkM2', 'LOCAL', TRUE, TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 3, 'hr01',       '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6I0qQ2V3Y8ExN1GpStO6Qf8Q4gkM2', 'LOCAL', TRUE, FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(4, 4, 'payroll01',  '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6I0qQ2V3Y8ExN1GpStO6Qf8Q4gkM2', 'LOCAL', TRUE, FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(5, 5, 'admin01',    '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6I0qQ2V3Y8ExN1GpStO6Qf8Q4gkM2', 'LOCAL', TRUE, TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO user_role (user_id, role_id, created_by, updated_by)
VALUES
(1, 1, 'SYSTEM', 'SYSTEM'),
(2, 2, 'SYSTEM', 'SYSTEM'),
(3, 3, 'SYSTEM', 'SYSTEM'),
(4, 4, 'SYSTEM', 'SYSTEM'),
(5, 5, 'SYSTEM', 'SYSTEM');

INSERT INTO leave_type (leave_type_id, leave_code, leave_name, paid_flag, requires_attachment, allow_negative_balance, approval_level_rule, status, created_by, updated_by)
VALUES
(1, 'ANNUAL', 'Annual Leave', TRUE, FALSE, FALSE, 'MANAGER_ONLY', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'SICK', 'Sick Leave', TRUE, TRUE, FALSE, 'MANAGER_HR', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(3, 'UNPAID', 'Unpaid Leave', FALSE, FALSE, TRUE, 'MANAGER_HR', 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO shift (shift_id, shift_code, shift_name, start_time, end_time, checkin_window_before_min, checkout_window_after_min, cross_day_flag, status, created_by, updated_by)
VALUES
(1, 'SHIFT-A', 'Office Morning Shift', '08:30:00', '17:30:00', 30, 45, FALSE, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO shift_assignment (assignment_id, employee_id, shift_id, work_date, location_code, published_flag, status, created_by, updated_by)
VALUES
(1, 1, 1, CURRENT_DATE, 'HQ-HCM', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 2, 1, CURRENT_DATE, 'HQ-HCM', TRUE, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO attendance_daily (
    attendance_daily_id, employee_id, work_date, planned_minutes, worked_minutes, late_minutes, early_leave_minutes,
    attendance_status, status, created_by, updated_by
)
VALUES
(1, 1, CURRENT_DATE, 540, 0, 0, 0, 'PENDING', 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 2, CURRENT_DATE, 540, 0, 0, 0, 'PENDING', 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO approval_flow (
    approval_flow_id, business_type, business_id, requester_employee_id, current_step_no, final_decision, submitted_at, due_at,
    status, created_by, updated_by
)
VALUES
(1, 'LEAVE_REQUEST', 1, 1, 1, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 day', 'PENDING', 'SYSTEM', 'SYSTEM');

INSERT INTO leave_request (
    leave_request_id, employee_id, leave_type_id, approval_flow_id, date_from, date_to, duration_days, reason_text,
    status, created_by, updated_by
)
VALUES
(1, 1, 1, 1, CURRENT_DATE + INTERVAL '7 day', CURRENT_DATE + INTERVAL '8 day', 2.0, 'Family trip',
 'SUBMITTED', 'SYSTEM', 'SYSTEM');

INSERT INTO approval_step (
    approval_step_id, approval_flow_id, step_no, approver_employee_id, approver_role_code, decision, status, created_by, updated_by
)
VALUES
(1, 1, 1, 2, 'MANAGER', NULL, 'PENDING', 'SYSTEM', 'SYSTEM');

INSERT INTO payroll_run (
    payroll_run_id, pay_period_code, company_code, snapshot_date, run_status, employee_count, status, created_by, updated_by
)
VALUES
(1, '2026-03', 'HRMAPP', CURRENT_DATE, 'PUBLISHED', 5, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO payslip (
    payslip_id, payroll_run_id, employee_id, gross_income, deduction_total, net_income, currency_code, status, created_by, updated_by
)
VALUES
(1, 1, 1, 25000000, 2500000, 22500000, 'VND', 'PUBLISHED', 'SYSTEM', 'SYSTEM');

INSERT INTO announcement (
    announcement_id, title, summary_text, body_html, audience_rule, priority_level, publish_at, expire_at, status, created_by, updated_by
)
VALUES
(1, 'Welcome to HRMApp', 'Phase 1 demo announcement', '<p>Welcome to HRMApp realtime demo.</p>', 'ALL', 'HIGH',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 day', 'PUBLISHED', 'SYSTEM', 'SYSTEM');

INSERT INTO notification_event (
    notification_event_id, event_code, recipient_user_id, title, message_text, deeplink_url, push_status, sent_at, status, created_by, updated_by
)
VALUES
(1, 'approval_pending', 2, 'New leave approval pending', 'Employee One submitted a leave request.', 'hrmapp://approvals/1',
 'SENT', CURRENT_TIMESTAMP, 'ACTIVE', 'SYSTEM', 'SYSTEM'),
(2, 'announcement_published', 1, 'Welcome to HRMApp', 'New company announcement published.', 'hrmapp://notifications/1',
 'SENT', CURRENT_TIMESTAMP, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO ticket_request (
    ticket_request_id, ticket_no, requester_employee_id, category_code, priority_level, owner_team, owner_employee_id,
    first_response_due_at, status, created_by, updated_by
)
VALUES
(1, 'TIC-0001', 1, 'HR_SUPPORT', 'MEDIUM', 'HR', 3, CURRENT_TIMESTAMP + INTERVAL '4 hour', 'OPEN', 'SYSTEM', 'SYSTEM');

INSERT INTO onboarding_task (
    onboarding_task_id, employee_id, task_name, task_owner_type, task_owner_id, due_at, mandatory_flag, status, created_by, updated_by
)
VALUES
(1, 1, 'Submit personal documents', 'HR', 3, CURRENT_TIMESTAMP + INTERVAL '5 day', TRUE, 'PENDING', 'SYSTEM', 'SYSTEM');

INSERT INTO training_assignment (
    training_assignment_id, employee_id, course_id, assigned_at, due_at, completion_percent, status, created_by, updated_by
)
VALUES
(1, 1, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '10 day', 20, 'ASSIGNED', 'SYSTEM', 'SYSTEM');

INSERT INTO performance_cycle (
    performance_cycle_id, cycle_name, year_label, self_review_due_at, manager_review_due_at, calibration_due_at, publish_at, rating_scheme,
    status, created_by, updated_by
)
VALUES
(1, 'Mid Year Review', '2026', CURRENT_TIMESTAMP + INTERVAL '20 day', CURRENT_TIMESTAMP + INTERVAL '30 day',
 CURRENT_TIMESTAMP + INTERVAL '40 day', CURRENT_TIMESTAMP + INTERVAL '50 day', '5_POINT', 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO goal_kpi (
    goal_kpi_id, performance_cycle_id, employee_id, goal_title, goal_type, weight_percent, target_value, actual_value,
    status, created_by, updated_by
)
VALUES
(1, 1, 1, 'Attendance Compliance', 'KPI', 20, 100, 95, 'ACTIVE', 'SYSTEM', 'SYSTEM');

INSERT INTO audit_log (
    audit_log_id, actor_user_id, action_code, resource_type, resource_id, before_json, after_json, ip_address, status, created_by, updated_by
)
VALUES
(1, 5, 'SEED_INIT', 'SYSTEM', 'BOOTSTRAP', '{}', '{"seed":"done"}', '127.0.0.1', 'ACTIVE', 'SYSTEM', 'SYSTEM');