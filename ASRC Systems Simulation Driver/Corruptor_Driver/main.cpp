// XML_Parser.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>

#include <boost/property_tree/xml_parser.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/foreach.hpp>

using boost::property_tree::ptree;

static bool  debuging_enabled = false;

template <typename Tree, typename Out>
Out enumerate_nodes(Tree const& pt, typename Tree::path_type path, Out out);
void read(std::istream& is);
void validateTest(ptree& test_tree);
void validateEvent(ptree const& event);
std::string validateVariant(ptree const& event);
void validateConstraints(ptree const& event);
void validateConstraintType(ptree const& event_constraint);
void validateConstraintBounds(ptree const& event_constraint);
void validateConstraintValue(ptree const& event_constraint);
void validateLinkManifest(ptree const& event, const std::string& variant);
void validateLinkManifestSteps(ptree const& link_manifest);
void validateLinkManifestStepCorruptionType(ptree const& link_manifest_step);
void validateLinkManifestStepErrorCount(ptree const& link_manifest_step);
void validateLinkManifestStepCooldownTime(ptree const& link_manifest_step);
void fail(const std::string& error);
void debug(const std::string& statement);
void removeWhitespace(std::string& sring_var);

int main()
{
    debuging_enabled = true;

    //Note path to your test file goes here.
    std::ifstream input("Every_Link_Manifest_Capable_Variant_EventTree.xml");
    read(input);

    return 0;
}

void  read(std::istream& is)
{
    // populate tree structure pt
    ptree test_tree;
    read_xml(is, test_tree);

    validateTest(test_tree);

    std::cout << "XML file read in sucessfully!";
}

void validateTest(ptree& test_tree)
{
    std::cout << "Starting Test Validation" << std::endl;
    std::vector<std::reference_wrapper<ptree const>> events;
    enumerate_nodes<ptree>(test_tree, "test.event", back_inserter(events));

    if (events.size() <= 0)
        fail("No events in test");

    for (ptree const& event : events)
    {
        validateEvent(event);
    }
}

void validateEvent(ptree const& event)
{
    debug("---------Event--------");
    auto variant = validateVariant(event);
    validateConstraints(event);
    validateLinkManifest(event, variant);
}

std::string validateVariant(ptree const& event)
{
    debug("---------Event Variant--------");

    std::vector<std::reference_wrapper<ptree const>> event_variants;
    enumerate_nodes<ptree>(event, "variant", back_inserter(event_variants));
    if (event_variants.size() != 1)
        fail("Events must have exactly 1 variant. This event has " + std::to_string(event_variants.size()));

    auto variant = event.get("variant", "");
    removeWhitespace(variant);

    //validate string
    auto valid_variant = (
        (variant == "NO_ERRORS") ||
        (variant == "ACQ_LINK_ERRORS") ||
        (variant == "ACQ_SUBPULSE_ERRORS") ||
        (variant == "EXIT_AFTER_ZONE") ||
        (variant == "EXIT_ON_REACQ") ||
        (variant == "EXIT_ON_TRANSITION") ||
        (variant == "LINK_ERRORS") ||
        (variant == "NO_ERRORS") ||
        (variant == "NO_ERRORS_IGNORE_REAQ") ||
        (variant == "PHASE_THREE_LINK_ERRORS") ||
        (variant == "PHASE_TRANSITION_DOWNLINK_ERRORS") ||
        (variant == "PHASE_TRANSITION_UPLINK_ERRORS") ||
        (variant == "SEND_SDM_PARAMETERS") ||
        (variant == "UPLINK_LATE") ||
        (variant == "UPLINK_LOST") ||
        (variant == "BURST_INTERRUPT") );

    if (false == valid_variant)
        fail("Invalid variant name: " + variant);

    return variant;
}

void validateConstraints(ptree const& event)
{
    std::vector<std::reference_wrapper<ptree const>> event_constraints;
    enumerate_nodes<ptree>(event, "constraint", back_inserter(event_constraints));
    for (ptree const& event_constraint : event_constraints)
    {
        debug("---------Event Constraint--------");
        validateConstraintType(event_constraint);
        validateConstraintBounds(event_constraint);
        validateConstraintValue(event_constraint);
    }
}

void validateConstraintType(ptree const& event_constraint)
{
    std::vector<std::reference_wrapper<ptree const>> event_constraint_types;
    enumerate_nodes<ptree>(event_constraint, "type", back_inserter(event_constraint_types));

    if (event_constraint_types.size() != 1) fail("Constraints must have exactly 1 type. This constraint has " + std::to_string(event_constraint_types.size()));

    //validate constraint type name
    auto constraint_type = event_constraint.get("type", "");
    removeWhitespace(constraint_type);

    auto valid_type = (
        (constraint_type == "acq_complete_flag") ||
        (constraint_type == "dwell_cnt") ||
        (constraint_type == "link_event_done") ||
        (constraint_type == "link_sequence_done") ||
        (constraint_type == "time_of_flight") ||
        (constraint_type == "time_until_intercept") ||
        (constraint_type == "uplink_lost_inject_count") ||
        (constraint_type == "endless_event_flag") ||
        (constraint_type == "link_manifest") ||
        (constraint_type == "acquisition_count") ||
        (constraint_type == "uplink_count") ||
        (constraint_type == "rate_change") ||
        (constraint_type == "ege_flag") ||
        (constraint_type == "time_based_error") );

    if (false == valid_type)
        fail("Constraint Type invalid: " + constraint_type);
}
void validateConstraintBounds(ptree const& event_constraint)
{
    std::vector<std::reference_wrapper<ptree const>> event_constraint_bounds;
    enumerate_nodes<ptree>(event_constraint, "bounds", back_inserter(event_constraint_bounds));

    if (event_constraint_bounds.size() != 1)
        fail("Constraints must have exactly 1 bound. This constraint has " + std::to_string(event_constraint_bounds.size()));

    //validate constraint type name
    auto constraint_bounds = event_constraint.get("bounds", "");
    removeWhitespace(constraint_bounds);

    auto valid_bounds = (
        (constraint_bounds == "CONSTRAINT_BOUND_DISABLED") ||
        (constraint_bounds == "CONSTRAINT_BOUND_GREATER") ||
        (constraint_bounds == "CONSTRAINT_BOUND_GREATER_OR_EQUAL") ||
        (constraint_bounds == "CONSTRAINT_BOUND_LESSER") ||
        (constraint_bounds == "CONSTRAINT_BOUND_LESSER_OR_EQUAL") ||
        (constraint_bounds == "CONSTRAINT_BOUND_EQUAL") );

    if (false == valid_bounds) fail("Constraint Bounds invalid: " + constraint_bounds);
}

void validateConstraintValue(ptree const& event_constraint)
{
    std::vector<std::reference_wrapper<ptree const>> event_constraint_value;
    enumerate_nodes<ptree>(event_constraint, "bounds", back_inserter(event_constraint_value));

    if (event_constraint_value.size() != 1) fail("Constraints must have exactly 1 value. This constraint has " + std::to_string(event_constraint_value.size()));

    //no restrictions on value. Must be double though.
}

void validateLinkManifest(ptree const& event, const std::string& variant)
{
    std::vector<std::reference_wrapper<ptree const>> event_link_manifests;
    enumerate_nodes<ptree>(event, "link_manifest", back_inserter(event_link_manifests));

    const auto num_of_man = event_link_manifests.size();
    if (num_of_man > 0)
    {
        debug("---------Event Link Manifest--------");
        bool is_link_manifest_variant = (

            (variant == "LINK_ERRORS") ||
            (variant == "ACQ_SUBPULSE_ERRORS") ||
            (variant == "EXIT_AFTER_ZONE") ||
            (variant == "EXIT_ON_REACQ") ||
            (variant == "EXIT_ON_TRANSITION") ||
            (variant == "PHASE_THREE_LINK_ERRORS"));

        if ((false == is_link_manifest_variant) && (num_of_man > 0))
            fail("This variant is not capable of having a link manifest: " + variant);

        if (num_of_man > 1)
            fail("There can only be 1 Link Manifest per Event. This Event has " + std::to_string(num_of_man));

        validateLinkManifestSteps(event_link_manifests[0]);
    }
}

void validateLinkManifestSteps(ptree const& link_manifest)
{
    std::vector<std::reference_wrapper<ptree const>> link_manifest_steps;
    enumerate_nodes<ptree>(link_manifest, "step", back_inserter(link_manifest_steps));

    if (link_manifest_steps.size() < 1)
        fail("Link Manifests must have at least 1 step. This manifest has " + std::to_string(link_manifest_steps.size()));

    for (ptree const& step : link_manifest_steps)
    {
        debug("---------Link Manifest Step--------");
        validateLinkManifestStepCorruptionType(step);
        validateLinkManifestStepErrorCount(step);
        validateLinkManifestStepCooldownTime(step);
    }
}

void validateLinkManifestStepCorruptionType(ptree const& link_manifest_step)
{
    std::vector<std::reference_wrapper<ptree const>> link_manifest_step_corruption_types;
    enumerate_nodes<ptree>(link_manifest_step, "corruption_type", back_inserter(link_manifest_step_corruption_types));

    auto num_of_corr_types = link_manifest_step_corruption_types.size();

    if (num_of_corr_types < 1)
        fail("Link Manifest Steps must have a minimum of 1 corruption_type. This Step has " + std::to_string(num_of_corr_types));

    //validate corruption_types
    for (auto& corruption_type : link_manifest_step_corruption_types)
    {
        debug("---------Link Manifest Step Corruption Type--------");
        auto corruption_type_value = corruption_type.get().data();
        removeWhitespace(corruption_type_value);

        auto is_valid_value = (
            (corruption_type_value == "UL_XMIT_ERROR") ||
            (corruption_type_value == "UL_GID_ERROR") ||
            (corruption_type_value == "UL_T2_ERROR") ||
            (corruption_type_value == "UL_T3_ERROR") ||
            (corruption_type_value == "UL_T4_ERROR") ||
            (corruption_type_value == "UL_ERROR") ||
            (corruption_type_value == "DL_ERROR") ||
            (corruption_type_value == "CROSS_CORR") ||
            (corruption_type_value == "SET_FORCED") ||
            (corruption_type_value == "SET_INHIBITED") ||
            (corruption_type_value == "SET_SEQUENTIAL") );

        if (false == is_valid_value)
            fail("Link Manifest Step corruption_type invalid: " + corruption_type_value);
    }
}

void validateLinkManifestStepErrorCount(ptree const& link_manifest_step)
{
    std::vector<std::reference_wrapper<ptree const>> link_manifest_step_error_counts;
    enumerate_nodes<ptree>(link_manifest_step, "link_error_count", back_inserter(link_manifest_step_error_counts));

    if (link_manifest_step_error_counts.size() != 1)
        fail("Link Manifest Steps must have exactly 1 error_count. This Step has " + std::to_string(link_manifest_step_error_counts.size()));

    //no restrictions on value. Must be int though.
}

void validateLinkManifestStepCooldownTime(ptree const& link_manifest_step)
{
    std::vector<std::reference_wrapper<ptree const>> link_manifest_step_cooldown_times;
    enumerate_nodes<ptree>(link_manifest_step, "cooldown_time", back_inserter(link_manifest_step_cooldown_times));

    if (link_manifest_step_cooldown_times.size() != 1)
        fail("Link Manifest Steps must have exactly 1 cooldown_time. This Step has " + std::to_string(link_manifest_step_cooldown_times.size()));

    //no restrictions on value. Must be double though.
}


void fail(const std::string& error)
{
    std::cout << "Test Read In Failed!!!" << std::endl;
    std::cout << "Reason: - " << error.c_str() << std::endl;
    exit(1);
}

void debug(const std::string& statement)
{
    if (debuging_enabled)
        std::cout << statement << std::endl;
}

void removeWhitespace(std::string& sring_var)
{
    sring_var.erase(std::remove_if(sring_var.begin(), sring_var.end(), isspace), sring_var.end());
}

template <typename Tree, typename Out>
Out enumerate_nodes(Tree const& pt, typename Tree::path_type path, Out out)
{
    if (path.empty())
        return out;

    if (path.single()) {
        auto name = path.reduce();
        for (auto& child : pt)
        {
            if (child.first == name)
                *out++ = child.second;
        }
    }
    else {
        auto head = path.reduce();
        for (auto& child : pt) {
            if (head == "*" || child.first == head) {
                out = enumerate_nodes(child.second, path, out);
            }
        }
    }

    return out;
}
