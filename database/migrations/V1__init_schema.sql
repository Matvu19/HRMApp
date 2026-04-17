CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE org_unit (
    org_unit_id BIGSERIAL PRIMARY KEY,
    parent_org_unit_id BIGINT NULL,
    org_code VARCHAR(50) NOT NULL UNIQUE,
    org_name VARCHAR(255) NOT NULL,
    org_level INTEGER NOT NULL DEFAULT 1,
    cost_center VARCHAR(100),
    location_code VARCHAR(100),
    head_employee_id BIGINT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE position (
    position_id BIGSERIAL PRIMARY KEY,
    position_code VARCHAR(50) NOT NULL UNIQUE,
    position_name VARCHAR(255) NOT NULL,
    job_family VARCHAR(100),
    grade_band VARCHAR(50),
    managerial_flag BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE employee_profile (
    employee_id BIGSERIAL PRIMARY KEY,
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    dob DATE,
    gender VARCHAR(20),
    work_email VARCHAR(255) UNIQUE,
    personal_phone VARCHAR(50),
    org_unit_id BIGINT NOT NULL,
    position_id BIGINT,
    manager_employee_id BIGINT,
    join_date DATE,
    avatar_url VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE role (
    role_id BIGSERIAL PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    scope_type VARCHAR(30) NOT NULL,
    is_system_role BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE permission_policy (
    policy_id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    resource_code VARCHAR(100) NOT NULL,
    action_code VARCHAR(100) NOT NULL,
    org_scope_rule VARCHAR(50),
    masking_rule VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE user_account (
    user_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    auth_provider VARCHAR(50) NOT NULL DEFAULT 'LOCAL',
    mfa_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    biometric_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at TIMESTAMP,
    lock_reason VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE user_role (
    user_role_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    UNIQUE (user_id, role_id)
);

CREATE TABLE device_session (
    session_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    device_id VARCHAR(150) NOT NULL,
    device_name VARCHAR(150),
    platform VARCHAR(50),
    push_token VARCHAR(255),
    refresh_token VARCHAR(255),
    last_seen_at TIMESTAMP,
    revoked_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE employment_contract (
    contract_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    contract_no VARCHAR(100) NOT NULL UNIQUE,
    contract_type VARCHAR(50) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE,
    base_salary NUMERIC(18,2) NOT NULL DEFAULT 0,
    legal_entity VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE shift (
    shift_id BIGSERIAL PRIMARY KEY,
    shift_code VARCHAR(50) NOT NULL UNIQUE,
    shift_name VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    checkin_window_before_min INTEGER NOT NULL DEFAULT 30,
    checkout_window_after_min INTEGER NOT NULL DEFAULT 30,
    cross_day_flag BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE shift_assignment (
    assignment_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    location_code VARCHAR(100),
    published_flag BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE attendance_event (
    event_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    assignment_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_time_device TIMESTAMP NOT NULL,
    event_time_server TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    geo_lat NUMERIC(10,7),
    geo_lng NUMERIC(10,7),
    device_id VARCHAR(150),
    idempotency_key VARCHAR(150),
    source_type VARCHAR(50) DEFAULT 'MOBILE',
    status VARCHAR(30) NOT NULL DEFAULT 'RECORDED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE attendance_daily (
    attendance_daily_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    planned_minutes INTEGER NOT NULL DEFAULT 0,
    worked_minutes INTEGER NOT NULL DEFAULT 0,
    late_minutes INTEGER NOT NULL DEFAULT 0,
    early_leave_minutes INTEGER NOT NULL DEFAULT 0,
    attendance_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    UNIQUE (employee_id, work_date)
);

CREATE TABLE leave_type (
    leave_type_id BIGSERIAL PRIMARY KEY,
    leave_code VARCHAR(50) NOT NULL UNIQUE,
    leave_name VARCHAR(255) NOT NULL,
    paid_flag BOOLEAN NOT NULL DEFAULT TRUE,
    requires_attachment BOOLEAN NOT NULL DEFAULT FALSE,
    allow_negative_balance BOOLEAN NOT NULL DEFAULT FALSE,
    approval_level_rule VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE approval_flow (
    approval_flow_id BIGSERIAL PRIMARY KEY,
    business_type VARCHAR(50) NOT NULL,
    business_id BIGINT NOT NULL,
    requester_employee_id BIGINT NOT NULL,
    current_step_no INTEGER NOT NULL DEFAULT 1,
    final_decision VARCHAR(30) DEFAULT 'PENDING',
    submitted_at TIMESTAMP,
    due_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE leave_request (
    leave_request_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type_id BIGINT NOT NULL,
    approval_flow_id BIGINT,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL,
    duration_days NUMERIC(5,2) NOT NULL,
    reason_text TEXT,
    attachment_file_id BIGINT,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE overtime_request (
    overtime_request_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    approval_flow_id BIGINT,
    work_date DATE NOT NULL,
    planned_start_at TIMESTAMP NOT NULL,
    planned_end_at TIMESTAMP NOT NULL,
    approved_minutes INTEGER DEFAULT 0,
    reason_text TEXT,
    cost_center VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE approval_step (
    approval_step_id BIGSERIAL PRIMARY KEY,
    approval_flow_id BIGINT NOT NULL,
    step_no INTEGER NOT NULL,
    approver_employee_id BIGINT,
    approver_role_code VARCHAR(50),
    decision VARCHAR(30),
    decision_note TEXT,
    acted_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE payroll_run (
    payroll_run_id BIGSERIAL PRIMARY KEY,
    pay_period_code VARCHAR(50) NOT NULL,
    company_code VARCHAR(50) NOT NULL,
    snapshot_date DATE NOT NULL,
    run_status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    employee_count INTEGER NOT NULL DEFAULT 0,
    published_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE payslip (
    payslip_id BIGSERIAL PRIMARY KEY,
    payroll_run_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    gross_income NUMERIC(18,2) NOT NULL DEFAULT 0,
    deduction_total NUMERIC(18,2) NOT NULL DEFAULT 0,
    net_income NUMERIC(18,2) NOT NULL DEFAULT 0,
    currency_code VARCHAR(10) NOT NULL DEFAULT 'VND',
    pdf_file_id BIGINT,
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE benefit_enrollment (
    benefit_enrollment_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    benefit_plan_id BIGINT,
    effective_from DATE NOT NULL,
    effective_to DATE,
    payroll_snapshot_flag BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE job_requisition (
    job_requisition_id BIGSERIAL PRIMARY KEY,
    requisition_code VARCHAR(50) NOT NULL UNIQUE,
    org_unit_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    headcount INTEGER NOT NULL DEFAULT 1,
    budget_amount NUMERIC(18,2),
    hiring_manager_id BIGINT,
    req_status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE candidate_profile (
    candidate_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    source_channel VARCHAR(100),
    current_stage VARCHAR(50),
    owner_recruiter_id BIGINT,
    latest_requisition_id BIGINT,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE onboarding_task (
    onboarding_task_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    task_name VARCHAR(255) NOT NULL,
    task_owner_type VARCHAR(50) NOT NULL,
    task_owner_id BIGINT,
    due_at TIMESTAMP,
    completion_at TIMESTAMP,
    mandatory_flag BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE training_assignment (
    training_assignment_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    course_id BIGINT,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_at TIMESTAMP,
    completion_percent NUMERIC(5,2) NOT NULL DEFAULT 0,
    score_value NUMERIC(5,2),
    completed_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'ASSIGNED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE performance_cycle (
    performance_cycle_id BIGSERIAL PRIMARY KEY,
    cycle_name VARCHAR(255) NOT NULL,
    year_label VARCHAR(20) NOT NULL,
    self_review_due_at TIMESTAMP,
    manager_review_due_at TIMESTAMP,
    calibration_due_at TIMESTAMP,
    publish_at TIMESTAMP,
    rating_scheme VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE goal_kpi (
    goal_kpi_id BIGSERIAL PRIMARY KEY,
    performance_cycle_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    goal_title VARCHAR(255) NOT NULL,
    goal_type VARCHAR(50),
    weight_percent NUMERIC(5,2),
    target_value NUMERIC(18,2),
    actual_value NUMERIC(18,2),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE announcement (
    announcement_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    summary_text VARCHAR(500),
    body_html TEXT,
    audience_rule VARCHAR(255),
    priority_level VARCHAR(30) DEFAULT 'NORMAL',
    publish_at TIMESTAMP,
    expire_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE notification_event (
    notification_event_id BIGSERIAL PRIMARY KEY,
    event_code VARCHAR(100) NOT NULL,
    recipient_user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message_text VARCHAR(1000),
    deeplink_url VARCHAR(500),
    push_status VARCHAR(30) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE ticket_request (
    ticket_request_id BIGSERIAL PRIMARY KEY,
    ticket_no VARCHAR(50) NOT NULL UNIQUE,
    requester_employee_id BIGINT NOT NULL,
    category_code VARCHAR(100),
    priority_level VARCHAR(30),
    owner_team VARCHAR(100),
    owner_employee_id BIGINT,
    first_response_due_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE asset_assignment (
    asset_assignment_id BIGSERIAL PRIMARY KEY,
    asset_item_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL,
    due_return_at TIMESTAMP,
    returned_at TIMESTAMP,
    condition_out VARCHAR(255),
    condition_in VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ASSIGNED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE document_file (
    document_file_id BIGSERIAL PRIMARY KEY,
    owner_type VARCHAR(50) NOT NULL,
    owner_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100),
    storage_key VARCHAR(500) NOT NULL,
    version_no INTEGER NOT NULL DEFAULT 1,
    retention_until DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE audit_log (
    audit_log_id BIGSERIAL PRIMARY KEY,
    actor_user_id BIGINT,
    action_code VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100) NOT NULL,
    resource_id VARCHAR(100),
    before_json TEXT,
    after_json TEXT,
    ip_address VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

ALTER TABLE org_unit
    ADD CONSTRAINT fk_org_unit_parent
    FOREIGN KEY (parent_org_unit_id) REFERENCES org_unit(org_unit_id);

ALTER TABLE employee_profile
    ADD CONSTRAINT fk_employee_org_unit
    FOREIGN KEY (org_unit_id) REFERENCES org_unit(org_unit_id);

ALTER TABLE employee_profile
    ADD CONSTRAINT fk_employee_position
    FOREIGN KEY (position_id) REFERENCES position(position_id);

ALTER TABLE employee_profile
    ADD CONSTRAINT fk_employee_manager
    FOREIGN KEY (manager_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE org_unit
    ADD CONSTRAINT fk_org_unit_head_employee
    FOREIGN KEY (head_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE permission_policy
    ADD CONSTRAINT fk_permission_policy_role
    FOREIGN KEY (role_id) REFERENCES role(role_id);

ALTER TABLE user_account
    ADD CONSTRAINT fk_user_account_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_user
    FOREIGN KEY (user_id) REFERENCES user_account(user_id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_role
    FOREIGN KEY (role_id) REFERENCES role(role_id);

ALTER TABLE device_session
    ADD CONSTRAINT fk_device_session_user
    FOREIGN KEY (user_id) REFERENCES user_account(user_id);

ALTER TABLE employment_contract
    ADD CONSTRAINT fk_employment_contract_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE shift_assignment
    ADD CONSTRAINT fk_shift_assignment_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE shift_assignment
    ADD CONSTRAINT fk_shift_assignment_shift
    FOREIGN KEY (shift_id) REFERENCES shift(shift_id);

ALTER TABLE attendance_event
    ADD CONSTRAINT fk_attendance_event_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE attendance_event
    ADD CONSTRAINT fk_attendance_event_assignment
    FOREIGN KEY (assignment_id) REFERENCES shift_assignment(assignment_id);

ALTER TABLE attendance_daily
    ADD CONSTRAINT fk_attendance_daily_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE approval_flow
    ADD CONSTRAINT fk_approval_flow_requester
    FOREIGN KEY (requester_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE leave_request
    ADD CONSTRAINT fk_leave_request_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE leave_request
    ADD CONSTRAINT fk_leave_request_leave_type
    FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id);

ALTER TABLE leave_request
    ADD CONSTRAINT fk_leave_request_approval_flow
    FOREIGN KEY (approval_flow_id) REFERENCES approval_flow(approval_flow_id);

ALTER TABLE overtime_request
    ADD CONSTRAINT fk_overtime_request_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE overtime_request
    ADD CONSTRAINT fk_overtime_request_approval_flow
    FOREIGN KEY (approval_flow_id) REFERENCES approval_flow(approval_flow_id);

ALTER TABLE approval_step
    ADD CONSTRAINT fk_approval_step_flow
    FOREIGN KEY (approval_flow_id) REFERENCES approval_flow(approval_flow_id);

ALTER TABLE approval_step
    ADD CONSTRAINT fk_approval_step_approver
    FOREIGN KEY (approver_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE payslip
    ADD CONSTRAINT fk_payslip_payroll_run
    FOREIGN KEY (payroll_run_id) REFERENCES payroll_run(payroll_run_id);

ALTER TABLE payslip
    ADD CONSTRAINT fk_payslip_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE benefit_enrollment
    ADD CONSTRAINT fk_benefit_enrollment_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE job_requisition
    ADD CONSTRAINT fk_job_requisition_org_unit
    FOREIGN KEY (org_unit_id) REFERENCES org_unit(org_unit_id);

ALTER TABLE job_requisition
    ADD CONSTRAINT fk_job_requisition_position
    FOREIGN KEY (position_id) REFERENCES position(position_id);

ALTER TABLE job_requisition
    ADD CONSTRAINT fk_job_requisition_hiring_manager
    FOREIGN KEY (hiring_manager_id) REFERENCES employee_profile(employee_id);

ALTER TABLE candidate_profile
    ADD CONSTRAINT fk_candidate_profile_owner
    FOREIGN KEY (owner_recruiter_id) REFERENCES employee_profile(employee_id);

ALTER TABLE candidate_profile
    ADD CONSTRAINT fk_candidate_profile_requisition
    FOREIGN KEY (latest_requisition_id) REFERENCES job_requisition(job_requisition_id);

ALTER TABLE onboarding_task
    ADD CONSTRAINT fk_onboarding_task_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE training_assignment
    ADD CONSTRAINT fk_training_assignment_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE goal_kpi
    ADD CONSTRAINT fk_goal_kpi_cycle
    FOREIGN KEY (performance_cycle_id) REFERENCES performance_cycle(performance_cycle_id);

ALTER TABLE goal_kpi
    ADD CONSTRAINT fk_goal_kpi_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE notification_event
    ADD CONSTRAINT fk_notification_event_user
    FOREIGN KEY (recipient_user_id) REFERENCES user_account(user_id);

ALTER TABLE ticket_request
    ADD CONSTRAINT fk_ticket_request_requester
    FOREIGN KEY (requester_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE ticket_request
    ADD CONSTRAINT fk_ticket_request_owner
    FOREIGN KEY (owner_employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE asset_assignment
    ADD CONSTRAINT fk_asset_assignment_employee
    FOREIGN KEY (employee_id) REFERENCES employee_profile(employee_id);

ALTER TABLE audit_log
    ADD CONSTRAINT fk_audit_log_actor
    FOREIGN KEY (actor_user_id) REFERENCES user_account(user_id);

CREATE INDEX idx_employee_profile_employee_code ON employee_profile(employee_code);
CREATE INDEX idx_user_account_username ON user_account(username);
CREATE INDEX idx_shift_assignment_work_date ON shift_assignment(work_date);
CREATE INDEX idx_attendance_event_event_time_server ON attendance_event(event_time_server);
CREATE INDEX idx_attendance_event_employee_time ON attendance_event(employee_id, event_time_server);
CREATE INDEX idx_payroll_run_pay_period_code ON payroll_run(pay_period_code);
CREATE INDEX idx_ticket_request_ticket_no ON ticket_request(ticket_no);
CREATE INDEX idx_job_requisition_requisition_code ON job_requisition(requisition_code);
CREATE INDEX idx_notification_event_recipient_user_id ON notification_event(recipient_user_id);
CREATE INDEX idx_leave_request_employee_id ON leave_request(employee_id);
CREATE INDEX idx_overtime_request_employee_id ON overtime_request(employee_id);