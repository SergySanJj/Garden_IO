package com.garden.endpoints;

public final class API {
    public static class Authorisation {
        public static final String ENDPOINT = "auth";

        public static class Params {
            public static final String LOGIN = "login";
            public static final String PASSWORD = "password";
        }
    }

    public static class Logout {
        public static final String ENDPOINT = "logout";

        public static class Params {
        }
    }

    public static class AddGardener {
        public static final String ENDPOINT = "add-gardener";

        public static class Params {
            public static final String LOGIN = "login";
            public static final String PASSWORD = "password";
            public static final String NAME = "name";
        }
    }

    public static class AddOwner {
        public static final String ENDPOINT = "add-owner";

        public static class Params {
            public static final String LOGIN = "login";
            public static final String PASSWORD = "password";
        }
    }

    public static class AddOrder {
        public static final String ENDPOINT = "add-order";

        public static class Params {
            public static final String TYPE = "order-type";
            public static final String QUANTITY = "quantity";
        }
    }

    public static class ApproveOrder {
        public static final String ENDPOINT = "approve-order";

        public static class Params {
            public static final String ORDERID = "order-id";
        }
    }

    public static class FinishJob {
        public static final String ENDPOINT = "finish-job";

        public static class Params {
            public static final String ORDERID = "order-id";
        }
    }

    public static class Content {
        public static final String ENDPOINT = "content";

        public static class Params {
            public static final String CONTENT = "content";

            public static final String ViewOrders = "view-orders";
            public static final String ViewUsers = "view-users";
            public static final String ViewRoles = "view-roles";

            public static final String ViewOpenJobs = "view-open-jobs";
            public static final String ViewFinishedJobs = "view-finished-jobs";

            public static final String ViewOpenOrders = "view-open-orders";
            public static final String ViewUnconfirmedOrders = "view-unconfirmed-orders";
        }
    }
}
